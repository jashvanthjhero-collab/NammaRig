import React, { useState, useMemo } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  FlatList,
  Modal,
  Platform,
  Switch,
  ScrollView,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import {
  Search,
  X,
  SlidersHorizontal,
  Check,
  Plus,
  Store,
  CheckCircle2,
  Cpu,
  Layers,
  ArrowRight,
  Sparkles,
} from 'lucide-react-native';
import { colors } from '../theme/colors';
import { useRigStore } from '../store/useRigStore';
import { formatInr } from '../lib/pricing';
import {
  SEED_PRODUCTS,
  getCheapestListing,
  getListingsForProduct,
  getShopById,
} from '../data/mockData';
import { BuildSlotCategory, Product, RootTabParamList } from '../types';

type NavigationProp = BottomTabNavigationProp<RootTabParamList>;

interface CategoryPill {
  label: string;
  category: BuildSlotCategory | null;
}

const CATEGORY_PILLS: CategoryPill[] = [
  { label: 'All Hardware', category: null },
  { label: 'Processors', category: 'cpu' },
  { label: 'Graphics', category: 'gpu' },
  { label: 'Motherboards', category: 'motherboard' },
  { label: 'Memory', category: 'ram' },
  { label: 'Storage', category: 'ssd' },
  { label: 'Coolers', category: 'cooler' },
  { label: 'Power', category: 'psu' },
  { label: 'Cabinets', category: 'cabinet' },
];

const AVAILABLE_BRANDS = [
  'AMD',
  'Intel',
  'NVIDIA',
  'ASUS',
  'MSI',
  'Corsair',
  'Samsung',
  'G.Skill',
  'Lian Li',
  'DeepCool',
  'Western Digital',
];

export function ComponentHunterScreen() {
  const insets = useSafeAreaInsets();
  const navigation = useNavigation<NavigationProp>();
  const { build, setSlot } = useRigStore();

  // Search & Filter State
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<BuildSlotCategory | null>(null);
  const [showFilterModal, setShowFilterModal] = useState(false);

  // Advanced Filters
  const [selectedBrands, setSelectedBrands] = useState<string[]>([]);
  const [maxBudget, setMaxBudget] = useState<number>(150000);
  const [inStockOnly, setInStockOnly] = useState<boolean>(false);

  // Shop Comparison Modal State
  const [selectedProductForComparison, setSelectedProductForComparison] = useState<Product | null>(null);

  // Temporary state for the recently added item feedback animation
  const [recentlyAddedId, setRecentlyAddedId] = useState<string | null>(null);

  // Toggle brand selection
  const handleToggleBrand = (brand: string) => {
    setSelectedBrands((prev) =>
      prev.includes(brand) ? prev.filter((b) => b !== brand) : [...prev, brand]
    );
  };

  const handleResetFilters = () => {
    setSelectedBrands([]);
    setMaxBudget(150000);
    setInStockOnly(false);
  };

  // Filtered products calculation
  const filteredProducts = useMemo(() => {
    return SEED_PRODUCTS.filter((product) => {
      // 1. Category Filter
      if (selectedCategory !== null && product.category !== selectedCategory) {
        return false;
      }

      // 2. Search Query Filter (name, brand, specs, socket)
      const q = searchQuery.trim().toLowerCase();
      if (q.length > 0) {
        const matchesName = product.name.toLowerCase().includes(q);
        const matchesBrand = product.brand.toLowerCase().includes(q);
        const matchesSpecs = product.specs.toLowerCase().includes(q);
        const matchesSocket = product.socketType?.toLowerCase().includes(q) ?? false;
        if (!matchesName && !matchesBrand && !matchesSpecs && !matchesSocket) {
          return false;
        }
      }

      // 3. Brand Filter
      if (selectedBrands.length > 0) {
        const matchesAnyBrand = selectedBrands.some((b) =>
          product.brand.toLowerCase().includes(b.toLowerCase())
        );
        if (!matchesAnyBrand) {
          return false;
        }
      }

      // 4. Budget & Stock Filters (based on listings)
      const cheapest = getCheapestListing(product.id);
      if (cheapest) {
        if (cheapest.priceInr > maxBudget) {
          return false;
        }
        if (inStockOnly && !cheapest.stock) {
          return false;
        }
      }

      return true;
    });
  }, [selectedCategory, searchQuery, selectedBrands, maxBudget, inStockOnly]);

  const handleAddProduct = (product: Product) => {
    const cheapest = getCheapestListing(product.id);
    if (!cheapest) return;

    setSlot(product.category, product.id, cheapest.shopId, cheapest.priceInr);
    setRecentlyAddedId(product.id);
    setTimeout(() => {
      setRecentlyAddedId((curr) => (curr === product.id ? null : curr));
    }, 2000);
  };

  const activeFiltersCount =
    selectedBrands.length + (maxBudget < 150000 ? 1 : 0) + (inStockOnly ? 1 : 0);

  return (
    <View style={[styles.container, { paddingTop: insets.top + 8 }]}>
      {/* 1. TOP HEADER & SEARCH BAR */}
      <View style={styles.headerContainer}>
        <View style={styles.titleRow}>
          <View>
            <Text style={styles.headerTagline}>PRICE HUNTER · CHENNAI</Text>
            <Text style={styles.headerTitle}>Component Hunter</Text>
          </View>

          <TouchableOpacity
            style={[
              styles.filterButton,
              activeFiltersCount > 0 && styles.filterButtonActive,
            ]}
            onPress={() => setShowFilterModal(true)}
            activeOpacity={0.75}
          >
            <SlidersHorizontal
              size={18}
              color={activeFiltersCount > 0 ? '#FFFFFF' : colors.ink}
            />
            {activeFiltersCount > 0 && (
              <View style={styles.filterCountBadge}>
                <Text style={styles.filterCountText}>{activeFiltersCount}</Text>
              </View>
            )}
          </TouchableOpacity>
        </View>

        {/* Search Input Box */}
        <View style={styles.searchBarWrapper}>
          <Search size={18} color={colors.muted} />
          <TextInput
            style={styles.searchInput}
            placeholder="Search AMD, RTX 5080, AM5, DDR5, Corsair..."
            placeholderTextColor={colors.muted}
            value={searchQuery}
            onChangeText={setSearchQuery}
            autoCapitalize="none"
            clearButtonMode="while-editing"
          />
          {searchQuery.length > 0 && (
            <TouchableOpacity onPress={() => setSearchQuery('')}>
              <X size={16} color={colors.muted} />
            </TouchableOpacity>
          )}
        </View>
      </View>

      {/* 2. HORIZONTAL SWIPEABLE CATEGORY PILLS */}
      <View style={styles.categoryPillsWrapper}>
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.categoryPillsScroll}
        >
          {CATEGORY_PILLS.map((pill) => {
            const isSelected = selectedCategory === pill.category;
            return (
              <TouchableOpacity
                key={pill.label}
                style={[
                  styles.categoryPill,
                  isSelected && styles.categoryPillActive,
                ]}
                activeOpacity={0.7}
                onPress={() => setSelectedCategory(pill.category)}
              >
                <Text
                  style={[
                    styles.categoryPillText,
                    isSelected && styles.categoryPillTextActive,
                  ]}
                >
                  {pill.label}
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>
      </View>

      {/* Active Filter Tags Indicator */}
      {activeFiltersCount > 0 && (
        <View style={styles.activeFilterBanner}>
          <Text style={styles.activeFilterBannerText}>
            Filters applied: {selectedBrands.join(', ')}
            {maxBudget < 150000 ? ` • Up to ${formatInr(maxBudget)}` : ''}
            {inStockOnly ? ' • In Stock Only' : ''}
          </Text>
          <TouchableOpacity onPress={handleResetFilters}>
            <Text style={styles.activeFilterResetText}>Reset</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* 3. PRODUCT CARDS LIST */}
      <FlatList
        data={filteredProducts}
        keyExtractor={(item) => item.id}
        contentContainerStyle={[
          styles.productListContent,
          { paddingBottom: insets.bottom + 100 },
        ]}
        showsVerticalScrollIndicator={false}
        ListEmptyComponent={
          <View style={styles.emptyContainer}>
            <Cpu size={42} color={colors.muted} />
            <Text style={styles.emptyTitle}>No Matching Hardware Found</Text>
            <Text style={styles.emptySubtitle}>
              Try adjusting your search query, clearing brand filters, or raising the budget slider.
            </Text>
            <TouchableOpacity
              style={styles.emptyResetButton}
              onPress={() => {
                setSearchQuery('');
                setSelectedCategory(null);
                handleResetFilters();
              }}
            >
              <Text style={styles.emptyResetButtonText}>Reset All Filters</Text>
            </TouchableOpacity>
          </View>
        }
        renderItem={({ item }) => {
          const cheapest = getCheapestListing(item.id);
          const shop = cheapest ? getShopById(cheapest.shopId) : undefined;
          const isEquipped = build[item.category]?.productId === item.id;
          const isJustAdded = recentlyAddedId === item.id;

          return (
            <View style={styles.productCard}>
              {/* Card Top: Category & Brand */}
              <View style={styles.cardTopRow}>
                <View style={styles.categoryBadge}>
                  <Text style={styles.categoryBadgeText}>
                    {item.category.toUpperCase()}
                  </Text>
                </View>
                <Text style={styles.brandTag}>{item.brand}</Text>
              </View>

              {/* Title & Detailed Specs */}
              <Text style={styles.productTitle}>{item.name}</Text>
              <Text style={styles.productSpecs}>{item.specs}</Text>

              {/* Pricing & Best Shop Row */}
              <View style={styles.cardPriceRow}>
                <View>
                  <Text style={styles.priceLabel}>RITCHIE ST BEST PRICE</Text>
                  <Text style={styles.priceValue}>
                    {cheapest ? formatInr(cheapest.priceInr) : 'Call for Quote'}
                  </Text>
                </View>

                {shop && (
                  <View style={styles.shopBadgeBox}>
                    <Store size={12} color={colors.greenLowest} />
                    <Text style={styles.shopBadgeText} numberOfLines={1}>
                      {shop.name}
                    </Text>
                  </View>
                )}
              </View>

              {/* Action Buttons: Compare Shops & Quick Add */}
              <View style={styles.cardActionsRow}>
                <TouchableOpacity
                  style={styles.compareButton}
                  activeOpacity={0.75}
                  onPress={() => setSelectedProductForComparison(item)}
                >
                  <Text style={styles.compareButtonText}>Compare Shops</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[
                    styles.addButton,
                    (isEquipped || isJustAdded) && styles.addButtonActive,
                  ]}
                  activeOpacity={0.8}
                  onPress={() => handleAddProduct(item)}
                >
                  {isEquipped || isJustAdded ? (
                    <>
                      <CheckCircle2 size={16} color="#FFFFFF" />
                      <Text style={styles.addButtonText}>✓ Added</Text>
                    </>
                  ) : (
                    <>
                      <Plus size={16} color="#FFFFFF" />
                      <Text style={styles.addButtonText}>+ Add</Text>
                    </>
                  )}
                </TouchableOpacity>
              </View>
            </View>
          );
        }}
      />

      {/* 4. FILTER MODAL / BOTTOM SHEET */}
      <Modal
        visible={showFilterModal}
        animationType="slide"
        transparent={true}
        onRequestClose={() => setShowFilterModal(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={[styles.modalSheet, { paddingBottom: insets.bottom + 20 }]}>
            {/* Modal Header */}
            <View style={styles.modalHeader}>
              <View>
                <Text style={styles.modalTagline}>FILTER CHENNAI CATALOG</Text>
                <Text style={styles.modalTitle}>Hardware Filters</Text>
              </View>
              <TouchableOpacity
                onPress={() => setShowFilterModal(false)}
                style={styles.modalCloseButton}
              >
                <X size={20} color={colors.ink} />
              </TouchableOpacity>
            </View>

            <ScrollView showsVerticalScrollIndicator={false}>
              {/* Max Budget Stepper / Slider */}
              <View style={styles.filterSection}>
                <View style={styles.filterSectionTitleRow}>
                  <Text style={styles.filterSectionTitle}>Max Budget Limit</Text>
                  <Text style={styles.budgetValueHighlight}>
                    {formatInr(maxBudget)}
                  </Text>
                </View>

                {/* Quick Budget Presets */}
                <View style={styles.budgetPresetsRow}>
                  {[15000, 35000, 75000, 150000].map((amount) => (
                    <TouchableOpacity
                      key={amount}
                      style={[
                        styles.budgetPresetPill,
                        maxBudget === amount && styles.budgetPresetPillActive,
                      ]}
                      onPress={() => setMaxBudget(amount)}
                    >
                      <Text
                        style={[
                          styles.budgetPresetText,
                          maxBudget === amount && styles.budgetPresetTextActive,
                        ]}
                      >
                        {formatInr(amount)}
                      </Text>
                    </TouchableOpacity>
                  ))}
                </View>
              </View>

              {/* Brand Pills Selector */}
              <View style={styles.filterSection}>
                <Text style={styles.filterSectionTitle}>Brands</Text>
                <View style={styles.brandPillsGrid}>
                  {AVAILABLE_BRANDS.map((brand) => {
                    const isSelected = selectedBrands.includes(brand);
                    return (
                      <TouchableOpacity
                        key={brand}
                        style={[
                          styles.brandPill,
                          isSelected && styles.brandPillActive,
                        ]}
                        activeOpacity={0.7}
                        onPress={() => handleToggleBrand(brand)}
                      >
                        <Text
                          style={[
                            styles.brandPillText,
                            isSelected && styles.brandPillTextActive,
                          ]}
                        >
                          {brand}
                        </Text>
                      </TouchableOpacity>
                    );
                  })}
                </View>
              </View>

              {/* In-Stock Only Toggle */}
              <View style={[styles.filterSection, styles.toggleSectionRow]}>
                <View>
                  <Text style={styles.filterSectionTitle}>In-Stock Only</Text>
                  <Text style={styles.toggleDesc}>
                    Show hardware ready for same-day Ritchie St pickup
                  </Text>
                </View>
                <Switch
                  value={inStockOnly}
                  onValueChange={setInStockOnly}
                  trackColor={{ false: colors.border, true: colors.greenLowest }}
                  thumbColor="#FFFFFF"
                />
              </View>
            </ScrollView>

            {/* Modal Bottom Actions */}
            <View style={styles.modalFooter}>
              <TouchableOpacity
                style={styles.modalResetButton}
                onPress={handleResetFilters}
              >
                <Text style={styles.modalResetButtonText}>Reset Filters</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.modalApplyButton}
                onPress={() => setShowFilterModal(false)}
              >
                <Text style={styles.modalApplyButtonText}>
                  Apply ({filteredProducts.length} Results)
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      {/* 5. SHOP COMPARISON MODAL */}
      {selectedProductForComparison && (
        <Modal
          visible={!!selectedProductForComparison}
          animationType="fade"
          transparent={true}
          onRequestClose={() => setSelectedProductForComparison(null)}
        >
          <View style={styles.modalOverlay}>
            <View style={[styles.compareModalSheet, { paddingBottom: insets.bottom + 20 }]}>
              <View style={styles.modalHeader}>
                <View style={{ flex: 1 }}>
                  <Text style={styles.modalTagline}>LOCAL DEALER COMPARISON</Text>
                  <Text style={styles.compareProductTitle} numberOfLines={1}>
                    {selectedProductForComparison.name}
                  </Text>
                </View>
                <TouchableOpacity
                  onPress={() => setSelectedProductForComparison(null)}
                  style={styles.modalCloseButton}
                >
                  <X size={20} color={colors.ink} />
                </TouchableOpacity>
              </View>

              <Text style={styles.comparePrompt}>
                Live quotes across Ritchie Street, Mount Road & T. Nagar:
              </Text>

              <ScrollView style={{ maxHeight: 320 }}>
                {getListingsForProduct(selectedProductForComparison.id)
                  .sort((a, b) => a.priceInr - b.priceInr)
                  .map((listing, index) => {
                    const shop = getShopById(listing.shopId);
                    const isLowest = index === 0;

                    return (
                      <View
                        key={listing.shopId}
                        style={[
                          styles.shopListingRow,
                          isLowest && styles.shopListingRowLowest,
                        ]}
                      >
                        <View style={{ flex: 1 }}>
                          <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6 }}>
                            <Text style={styles.shopListingName}>
                              {shop?.name ?? listing.shopId}
                            </Text>
                            {isLowest && (
                              <View style={styles.lowestBadgePill}>
                                <Text style={styles.lowestBadgeText}>LOWEST</Text>
                              </View>
                            )}
                          </View>
                          <Text style={styles.shopListingLandmark}>
                            {shop?.landmark} • {listing.warrantyYears ?? 3}Y Warranty
                          </Text>
                        </View>

                        <View style={{ alignItems: 'flex-end' }}>
                          <Text
                            style={[
                              styles.shopListingPrice,
                              isLowest && { color: colors.greenLowest },
                            ]}
                          >
                            {formatInr(listing.priceInr)}
                          </Text>
                          <Text style={styles.stockLabel}>
                            {listing.stock ? 'In Stock' : 'Pre-Order'}
                          </Text>
                        </View>
                      </View>
                    );
                  })}
              </ScrollView>

              <TouchableOpacity
                style={styles.compareDoneButton}
                onPress={() => {
                  handleAddProduct(selectedProductForComparison);
                  setSelectedProductForComparison(null);
                }}
              >
                <Text style={styles.compareDoneButtonText}>
                  Add Best Deal to Rig ➔
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.canvas,
  },
  headerContainer: {
    paddingHorizontal: 16,
    marginBottom: 8,
  },
  titleRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  headerTagline: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 1.2,
  },
  headerTitle: {
    fontSize: 22,
    fontWeight: '900',
    color: colors.ink,
  },
  filterButton: {
    width: 40,
    height: 40,
    borderRadius: 12,
    backgroundColor: colors.card,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: colors.border,
  },
  filterButtonActive: {
    backgroundColor: colors.violet,
    borderColor: colors.violet,
  },
  filterCountBadge: {
    position: 'absolute',
    top: -4,
    right: -4,
    width: 17,
    height: 17,
    borderRadius: 9,
    backgroundColor: colors.greenLowest,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1.5,
    borderColor: '#FFFFFF',
  },
  filterCountText: {
    color: '#FFFFFF',
    fontSize: 9,
    fontWeight: '900',
  },
  searchBarWrapper: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.card,
    borderRadius: 14,
    paddingHorizontal: 12,
    paddingVertical: 10,
    borderWidth: 1,
    borderColor: colors.border,
    gap: 8,
  },
  searchInput: {
    flex: 1,
    fontSize: 13,
    color: colors.ink,
    padding: 0,
  },
  categoryPillsWrapper: {
    marginBottom: 8,
  },
  categoryPillsScroll: {
    paddingHorizontal: 16,
    gap: 8,
  },
  categoryPill: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: colors.card,
    borderWidth: 1,
    borderColor: colors.border,
  },
  categoryPillActive: {
    backgroundColor: colors.violet,
    borderColor: colors.violet,
  },
  categoryPillText: {
    fontSize: 12,
    fontWeight: '700',
    color: colors.ink,
  },
  categoryPillTextActive: {
    color: '#FFFFFF',
  },
  activeFilterBanner: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 16,
    paddingVertical: 6,
    marginHorizontal: 16,
    marginBottom: 8,
    borderRadius: 8,
  },
  activeFilterBannerText: {
    fontSize: 11,
    fontWeight: '600',
    color: colors.violet,
    flex: 1,
  },
  activeFilterResetText: {
    fontSize: 11,
    fontWeight: '800',
    color: colors.violet,
    marginLeft: 8,
  },
  productListContent: {
    paddingHorizontal: 16,
    gap: 12,
    paddingTop: 4,
  },
  productCard: {
    backgroundColor: colors.card,
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: colors.border,
    ...Platform.select({
      ios: {
        shadowColor: colors.ink,
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.05,
        shadowRadius: 10,
      },
      android: {
        elevation: 2,
      },
    }),
  },
  cardTopRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  categoryBadge: {
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 7,
    paddingVertical: 3,
    borderRadius: 5,
  },
  categoryBadgeText: {
    fontSize: 9.5,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 0.5,
  },
  brandTag: {
    fontSize: 11,
    fontWeight: '700',
    color: colors.muted,
  },
  productTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 3,
  },
  productSpecs: {
    fontSize: 12,
    color: colors.muted,
    lineHeight: 16.5,
    marginBottom: 12,
  },
  cardPriceRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-end',
    backgroundColor: colors.canvas,
    borderRadius: 12,
    padding: 10,
    marginBottom: 12,
  },
  priceLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.6,
    marginBottom: 2,
  },
  priceValue: {
    fontSize: 18,
    fontWeight: '900',
    color: colors.ink,
  },
  shopBadgeBox: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.greenBg,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
    gap: 4,
    maxWidth: 160,
  },
  shopBadgeText: {
    fontSize: 10.5,
    fontWeight: '700',
    color: colors.greenLowest,
  },
  cardActionsRow: {
    flexDirection: 'row',
    gap: 10,
  },
  compareButton: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
    borderWidth: 1.5,
    borderColor: colors.border,
    paddingVertical: 10,
    borderRadius: 10,
  },
  compareButtonText: {
    fontSize: 12,
    fontWeight: '700',
    color: colors.ink,
  },
  addButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.violet,
    paddingVertical: 10,
    borderRadius: 10,
    gap: 5,
  },
  addButtonActive: {
    backgroundColor: colors.greenLowest,
  },
  addButtonText: {
    fontSize: 12,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  emptyContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 40,
    paddingHorizontal: 20,
  },
  emptyTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: colors.ink,
    marginTop: 12,
    marginBottom: 6,
  },
  emptySubtitle: {
    fontSize: 12.5,
    color: colors.muted,
    textAlign: 'center',
    lineHeight: 18,
    marginBottom: 16,
  },
  emptyResetButton: {
    backgroundColor: colors.violet,
    paddingHorizontal: 16,
    paddingVertical: 9,
    borderRadius: 10,
  },
  emptyResetButtonText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '700',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(9, 13, 22, 0.55)',
    justifyContent: 'flex-end',
  },
  modalSheet: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 26,
    borderTopRightRadius: 26,
    paddingHorizontal: 18,
    paddingTop: 18,
    maxHeight: '80%',
  },
  compareModalSheet: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 26,
    borderTopRightRadius: 26,
    paddingHorizontal: 18,
    paddingTop: 18,
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 14,
  },
  modalTagline: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 1,
  },
  modalTitle: {
    fontSize: 19,
    fontWeight: '900',
    color: colors.ink,
  },
  modalCloseButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    backgroundColor: colors.canvas,
    alignItems: 'center',
    justifyContent: 'center',
  },
  filterSection: {
    marginBottom: 18,
  },
  filterSectionTitleRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  filterSectionTitle: {
    fontSize: 14,
    fontWeight: '800',
    color: colors.ink,
  },
  budgetValueHighlight: {
    fontSize: 15,
    fontWeight: '900',
    color: colors.violet,
  },
  budgetPresetsRow: {
    flexDirection: 'row',
    gap: 8,
  },
  budgetPresetPill: {
    flex: 1,
    paddingVertical: 8,
    borderRadius: 10,
    backgroundColor: colors.canvas,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: colors.border,
  },
  budgetPresetPillActive: {
    backgroundColor: colors.violetSoft,
    borderColor: colors.violet,
  },
  budgetPresetText: {
    fontSize: 11,
    fontWeight: '700',
    color: colors.ink,
  },
  budgetPresetTextActive: {
    color: colors.violet,
  },
  brandPillsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginTop: 8,
  },
  brandPill: {
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 16,
    backgroundColor: colors.canvas,
    borderWidth: 1,
    borderColor: colors.border,
  },
  brandPillActive: {
    backgroundColor: colors.violet,
    borderColor: colors.violet,
  },
  brandPillText: {
    fontSize: 11.5,
    fontWeight: '700',
    color: colors.ink,
  },
  brandPillTextActive: {
    color: '#FFFFFF',
  },
  toggleSectionRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  toggleDesc: {
    fontSize: 11,
    color: colors.muted,
    marginTop: 2,
  },
  modalFooter: {
    flexDirection: 'row',
    gap: 12,
    paddingTop: 12,
    borderTopWidth: 1,
    borderTopColor: colors.border,
  },
  modalResetButton: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 12,
    borderWidth: 1.5,
    borderColor: colors.border,
    alignItems: 'center',
  },
  modalResetButtonText: {
    fontSize: 13,
    fontWeight: '700',
    color: colors.ink,
  },
  modalApplyButton: {
    flex: 1.5,
    paddingVertical: 12,
    borderRadius: 12,
    backgroundColor: colors.violet,
    alignItems: 'center',
  },
  modalApplyButtonText: {
    fontSize: 13,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  compareProductTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: colors.ink,
  },
  comparePrompt: {
    fontSize: 12,
    color: colors.muted,
    marginBottom: 10,
  },
  shopListingRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 10,
    paddingHorizontal: 12,
    backgroundColor: colors.canvas,
    borderRadius: 12,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: colors.border,
  },
  shopListingRowLowest: {
    borderColor: colors.greenLowest,
    backgroundColor: colors.greenBg,
  },
  shopListingName: {
    fontSize: 13,
    fontWeight: '800',
    color: colors.ink,
  },
  lowestBadgePill: {
    backgroundColor: colors.greenLowest,
    paddingHorizontal: 5,
    paddingVertical: 2,
    borderRadius: 4,
  },
  lowestBadgeText: {
    fontSize: 8.5,
    fontWeight: '900',
    color: '#FFFFFF',
  },
  shopListingLandmark: {
    fontSize: 10.5,
    color: colors.muted,
    marginTop: 2,
  },
  shopListingPrice: {
    fontSize: 15,
    fontWeight: '900',
    color: colors.ink,
  },
  stockLabel: {
    fontSize: 10,
    color: colors.muted,
    fontWeight: '600',
  },
  compareDoneButton: {
    backgroundColor: colors.violet,
    borderRadius: 12,
    paddingVertical: 12,
    alignItems: 'center',
    marginTop: 14,
  },
  compareDoneButtonText: {
    color: '#FFFFFF',
    fontSize: 13,
    fontWeight: '800',
  },
});
