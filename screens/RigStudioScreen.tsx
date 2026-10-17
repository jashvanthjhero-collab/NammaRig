import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Modal,
  FlatList,
  Platform,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import {
  Cpu,
  Layers,
  Zap,
  CheckCircle2,
  Plus,
  RefreshCw,
  Trash2,
  Receipt,
  Store,
  X,
  Sparkles,
  ShieldCheck,
  ChevronRight,
  HardDrive,
  Fan,
  Box as BoxIcon,
  CircuitBoard,
} from 'lucide-react-native';
import { colors } from '../theme/colors';
import { useRigStore } from '../store/useRigStore';
import { formatInr } from '../lib/pricing';
import {
  SEED_PRODUCTS,
  getCheapestListing,
  getProductById,
  getShopById,
} from '../data/mockData';
import { BuildSlotCategory, Product, RootTabParamList } from '../types';

type NavigationProp = BottomTabNavigationProp<RootTabParamList>;

interface SlotMeta {
  category: BuildSlotCategory;
  title: string;
  shortCode: string;
  icon: any;
}

const SLOT_CONFIG: SlotMeta[] = [
  { category: 'cpu', title: 'Processor (CPU)', shortCode: 'CPU', icon: Cpu },
  { category: 'motherboard', title: 'Motherboard', shortCode: 'MOBO', icon: CircuitBoard },
  { category: 'gpu', title: 'Graphics Card (GPU)', shortCode: 'GPU', icon: Layers },
  { category: 'cooler', title: 'CPU Cooler', shortCode: 'CLR', icon: Fan },
  { category: 'ram', title: 'Memory (RAM)', shortCode: 'RAM', icon: Cpu },
  { category: 'ssd', title: 'Storage (NVMe SSD)', shortCode: 'SSD', icon: HardDrive },
  { category: 'psu', title: 'Power Supply (PSU)', shortCode: 'PSU', icon: Zap },
  { category: 'cabinet', title: 'Chassis Cabinet', shortCode: 'CASE', icon: BoxIcon },
];

export function RigStudioScreen() {
  const insets = useSafeAreaInsets();
  const navigation = useNavigation<NavigationProp>();
  const {
    build,
    setSlot,
    removeSlot,
    clearBuild,
    loadSeedPreset,
    getTotalPrice,
    getFilledCount,
  } = useRigStore();

  const [activePickerCategory, setActivePickerCategory] = useState<BuildSlotCategory | null>(null);

  const filledCount = getFilledCount();
  const totalPrice = getTotalPrice();

  // Filter products for active category in picker modal
  const availableCategoryProducts = activePickerCategory
    ? SEED_PRODUCTS.filter((p) => p.category === activePickerCategory)
    : [];

  const handleSelectProduct = (product: Product) => {
    const cheapest = getCheapestListing(product.id);
    if (!cheapest) return;

    setSlot(product.category, product.id, cheapest.shopId, cheapest.priceInr);
    setActivePickerCategory(null);
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top + 8 }]}>
      {/* 1. TOP HEADER & WORKBENCH META */}
      <View style={styles.topHeader}>
        <View>
          <View style={styles.badgeRow}>
            <Text style={styles.headerTagline}>8-COMPONENT CAD MATRIX</Text>
            <View style={styles.blueprintCodeBadge}>
              <Text style={styles.blueprintCodeText}>NR-BLD-8821-AM5</Text>
            </View>
          </View>
          <Text style={styles.headerTitle}>Rig Studio CAD</Text>
        </View>

        <View style={styles.headerActions}>
          <TouchableOpacity
            style={styles.presetButton}
            onPress={loadSeedPreset}
            activeOpacity={0.75}
          >
            <Sparkles size={14} color={colors.violet} />
            <Text style={styles.presetButtonText}>Load Preset</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.clearButton}
            onPress={clearBuild}
            activeOpacity={0.75}
          >
            <Trash2 size={16} color={colors.muted} />
          </TouchableOpacity>
        </View>
      </View>

      {/* 2. 8-SLOT INTERACTIVE HARDWARE MATRIX */}
      <ScrollView
        style={styles.slotsScroll}
        contentContainerStyle={[
          styles.slotsScrollContent,
          { paddingBottom: insets.bottom + 120 },
        ]}
        showsVerticalScrollIndicator={false}
      >
        {SLOT_CONFIG.map((slotMeta) => {
          const slotData = build[slotMeta.category];
          const product = slotData?.productId ? getProductById(slotData.productId) : undefined;
          const shop = slotData?.shopId ? getShopById(slotData.shopId) : undefined;
          const isFilled = !!product;
          const IconComp = slotMeta.icon;

          return (
            <View
              key={slotMeta.category}
              style={[
                styles.slotCard,
                isFilled && styles.slotCardFilled,
              ]}
            >
              {/* Slot Indicator Left Box */}
              <View
                style={[
                  styles.slotIndicatorBox,
                  isFilled && styles.slotIndicatorBoxFilled,
                ]}
              >
                <IconComp
                  size={18}
                  color={isFilled ? colors.violet : colors.muted}
                />
                <Text
                  style={[
                    styles.slotShortCode,
                    isFilled && styles.slotShortCodeFilled,
                  ]}
                >
                  {slotMeta.shortCode}
                </Text>
              </View>

              {/* Slot Body Details */}
              <View style={styles.slotBody}>
                <View style={styles.slotTitleRow}>
                  <Text style={styles.slotTitle}>{slotMeta.title}</Text>
                  {isFilled && (
                    <View style={styles.verifiedFitBadge}>
                      <CheckCircle2 size={10} color={colors.greenLowest} />
                      <Text style={styles.verifiedFitText}>FIT OK</Text>
                    </View>
                  )}
                </View>

                {isFilled ? (
                  <>
                    <Text style={styles.partName} numberOfLines={1}>
                      {product.name}
                    </Text>
                    <View style={styles.partMetaRow}>
                      <Text style={styles.partPrice}>
                        {formatInr(slotData.price ?? 0)}
                      </Text>
                      {shop && (
                        <View style={styles.shopMetaPill}>
                          <Store size={10} color={colors.muted} />
                          <Text style={styles.shopMetaText} numberOfLines={1}>
                            {shop.name}
                          </Text>
                        </View>
                      )}
                    </View>
                  </>
                ) : (
                  <Text style={styles.emptySlotPlaceholder}>
                    Slot empty — Tap to pick component
                  </Text>
                )}
              </View>

              {/* Action Buttons Right */}
              <View style={styles.slotActionColumn}>
                {isFilled ? (
                  <View style={styles.filledActionGroup}>
                    <TouchableOpacity
                      style={styles.swapButton}
                      activeOpacity={0.7}
                      onPress={() => setActivePickerCategory(slotMeta.category)}
                    >
                      <RefreshCw size={13} color={colors.violet} />
                      <Text style={styles.swapButtonText}>Swap</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                      style={styles.removeSlotButton}
                      activeOpacity={0.7}
                      onPress={() => removeSlot(slotMeta.category)}
                    >
                      <X size={14} color={colors.muted} />
                    </TouchableOpacity>
                  </View>
                ) : (
                  <TouchableOpacity
                    style={styles.pickButton}
                    activeOpacity={0.8}
                    onPress={() => setActivePickerCategory(slotMeta.category)}
                  >
                    <Plus size={14} color="#FFFFFF" />
                    <Text style={styles.pickButtonText}>+ Pick</Text>
                  </TouchableOpacity>
                )}
              </View>
            </View>
          );
        })}
      </ScrollView>

      {/* 3. BOTTOM STICKY CAD TITLE BLOCK */}
      <View
        style={[
          styles.cadTitleBlockWrapper,
          { bottom: Math.max(insets.bottom, 12) + 70 },
        ]}
      >
        <View style={styles.cadTitleBlockContainer}>
          <View style={styles.cadMetaSection}>
            <View style={styles.cadSpecBadgeRow}>
              <View style={styles.specDot} />
              <Text style={styles.cadBlueprintRef}>NR-BLD-8821-AM5</Text>
              <Text style={styles.cadDivider}>•</Text>
              <Text style={styles.cadSlotsFraction}>
                {filledCount}/8 SLOTS
              </Text>
            </View>
            <Text style={styles.cadTotalPrice}>
              {formatInr(totalPrice)}
            </Text>
          </View>

          <TouchableOpacity
            style={styles.shoppingSlipAction}
            activeOpacity={0.85}
            onPress={() => navigation.navigate('Price Pulse')}
          >
            <Receipt size={16} color="#FFFFFF" />
            <Text style={styles.shoppingSlipActionText}>Get Shopping Slip</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* 4. HARDWARE PICKER MODAL */}
      <Modal
        visible={!!activePickerCategory}
        animationType="slide"
        transparent={true}
        onRequestClose={() => setActivePickerCategory(null)}
      >
        <View style={styles.modalOverlay}>
          <View
            style={[
              styles.pickerModalSheet,
              { paddingBottom: insets.bottom + 20 },
            ]}
          >
            {/* Modal Header */}
            <View style={styles.modalHeader}>
              <View>
                <Text style={styles.modalTagline}>
                  SELECT {activePickerCategory?.toUpperCase()}
                </Text>
                <Text style={styles.modalTitle}>
                  Compatible Ritchie St Hardware
                </Text>
              </View>
              <TouchableOpacity
                onPress={() => setActivePickerCategory(null)}
                style={styles.modalCloseButton}
              >
                <X size={20} color={colors.ink} />
              </TouchableOpacity>
            </View>

            {/* List of Category Products */}
            <FlatList
              data={availableCategoryProducts}
              keyExtractor={(item) => item.id}
              contentContainerStyle={styles.pickerListContent}
              renderItem={({ item }) => {
                const cheapest = getCheapestListing(item.id);
                const isSelected =
                  activePickerCategory &&
                  build[activePickerCategory]?.productId === item.id;

                return (
                  <TouchableOpacity
                    style={[
                      styles.pickerItemCard,
                      isSelected && styles.pickerItemCardSelected,
                    ]}
                    activeOpacity={0.8}
                    onPress={() => handleSelectProduct(item)}
                  >
                    <View style={{ flex: 1 }}>
                      <View style={styles.pickerItemBrandRow}>
                        <Text style={styles.pickerItemBrand}>{item.brand}</Text>
                        {item.socketType && (
                          <View style={styles.socketPill}>
                            <Text style={styles.socketPillText}>
                              {item.socketType}
                            </Text>
                          </View>
                        )}
                      </View>
                      <Text style={styles.pickerItemName}>{item.name}</Text>
                      <Text style={styles.pickerItemSpecs} numberOfLines={2}>
                        {item.specs}
                      </Text>
                    </View>

                    <View style={styles.pickerItemPriceColumn}>
                      <Text style={styles.pickerItemPrice}>
                        {cheapest ? formatInr(cheapest.priceInr) : '—'}
                      </Text>
                      <View
                        style={[
                          styles.pickerSelectBadge,
                          isSelected && styles.pickerSelectBadgeActive,
                        ]}
                      >
                        <Text
                          style={[
                            styles.pickerSelectBadgeText,
                            isSelected && styles.pickerSelectBadgeTextActive,
                          ]}
                        >
                          {isSelected ? 'Equipped' : 'Select'}
                        </Text>
                      </View>
                    </View>
                  </TouchableOpacity>
                );
              }}
            />
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.canvas,
  },
  topHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 16,
    marginBottom: 12,
  },
  badgeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginBottom: 2,
  },
  headerTagline: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 1.1,
  },
  blueprintCodeBadge: {
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 6,
    paddingVertical: 2,
    borderRadius: 4,
  },
  blueprintCodeText: {
    fontSize: 9,
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontWeight: '800',
    color: colors.violet,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: '900',
    color: colors.ink,
    letterSpacing: -0.4,
  },
  headerActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  presetButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.card,
    borderWidth: 1,
    borderColor: colors.border,
    paddingHorizontal: 10,
    paddingVertical: 7,
    borderRadius: 10,
    gap: 4,
  },
  presetButtonText: {
    fontSize: 11.5,
    fontWeight: '700',
    color: colors.violet,
  },
  clearButton: {
    width: 34,
    height: 34,
    borderRadius: 10,
    backgroundColor: colors.card,
    borderWidth: 1,
    borderColor: colors.border,
    alignItems: 'center',
    justifyContent: 'center',
  },
  slotsScroll: {
    flex: 1,
  },
  slotsScrollContent: {
    paddingHorizontal: 16,
    gap: 10,
  },
  slotCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.card,
    borderRadius: 16,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.border,
    ...Platform.select({
      ios: {
        shadowColor: colors.ink,
        shadowOffset: { width: 0, height: 3 },
        shadowOpacity: 0.04,
        shadowRadius: 8,
      },
      android: {
        elevation: 2,
      },
    }),
  },
  slotCardFilled: {
    borderColor: colors.border,
  },
  slotIndicatorBox: {
    width: 44,
    height: 48,
    borderRadius: 12,
    backgroundColor: colors.canvas,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 12,
  },
  slotIndicatorBoxFilled: {
    backgroundColor: colors.violetSoft,
  },
  slotShortCode: {
    fontSize: 8.5,
    fontWeight: '800',
    color: colors.muted,
    marginTop: 2,
    letterSpacing: 0.5,
  },
  slotShortCodeFilled: {
    color: colors.violet,
  },
  slotBody: {
    flex: 1,
  },
  slotTitleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginBottom: 2,
  },
  slotTitle: {
    fontSize: 10.5,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.5,
  },
  verifiedFitBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.greenBg,
    paddingHorizontal: 5,
    paddingVertical: 1.5,
    borderRadius: 4,
    gap: 3,
  },
  verifiedFitText: {
    fontSize: 8,
    fontWeight: '800',
    color: colors.greenLowest,
  },
  partName: {
    fontSize: 14,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 3,
  },
  partMetaRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  partPrice: {
    fontSize: 13,
    fontWeight: '900',
    color: colors.greenLowest,
  },
  shopMetaPill: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
  },
  shopMetaText: {
    fontSize: 10.5,
    color: colors.muted,
    fontWeight: '500',
    maxWidth: 120,
  },
  emptySlotPlaceholder: {
    fontSize: 12,
    color: colors.muted,
    fontStyle: 'italic',
    marginTop: 2,
  },
  slotActionColumn: {
    marginLeft: 10,
    alignItems: 'flex-end',
  },
  filledActionGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  swapButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 9,
    paddingVertical: 6,
    borderRadius: 8,
    gap: 4,
  },
  swapButtonText: {
    fontSize: 11,
    fontWeight: '700',
    color: colors.violet,
  },
  removeSlotButton: {
    width: 28,
    height: 28,
    borderRadius: 14,
    backgroundColor: colors.canvas,
    alignItems: 'center',
    justifyContent: 'center',
  },
  pickButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violet,
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 8,
    gap: 4,
  },
  pickButtonText: {
    fontSize: 11.5,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  cadTitleBlockWrapper: {
    position: 'absolute',
    left: 14,
    right: 14,
    zIndex: 90,
  },
  cadTitleBlockContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: colors.card,
    borderRadius: 18,
    paddingVertical: 12,
    paddingHorizontal: 14,
    borderWidth: 1.5,
    borderColor: colors.border,
    shadowColor: colors.ink,
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.12,
    shadowRadius: 14,
    elevation: 8,
  },
  cadMetaSection: {},
  cadSpecBadgeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 5,
    marginBottom: 2,
  },
  specDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: colors.greenLowest,
  },
  cadBlueprintRef: {
    fontSize: 9.5,
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
    fontWeight: '800',
    color: colors.muted,
  },
  cadDivider: {
    fontSize: 10,
    color: colors.muted,
  },
  cadSlotsFraction: {
    fontSize: 9.5,
    fontWeight: '800',
    color: colors.violet,
  },
  cadTotalPrice: {
    fontSize: 18,
    fontWeight: '900',
    color: colors.ink,
    letterSpacing: -0.3,
  },
  shoppingSlipAction: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violet,
    paddingHorizontal: 14,
    paddingVertical: 10,
    borderRadius: 12,
    gap: 6,
    shadowColor: colors.violet,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.35,
    shadowRadius: 8,
    elevation: 4,
  },
  shoppingSlipActionText: {
    fontSize: 12.5,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(9, 13, 22, 0.55)',
    justifyContent: 'flex-end',
  },
  pickerModalSheet: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 26,
    borderTopRightRadius: 26,
    paddingHorizontal: 18,
    paddingTop: 18,
    maxHeight: '75%',
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
    fontSize: 18,
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
  pickerListContent: {
    gap: 10,
    paddingBottom: 20,
  },
  pickerItemCard: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: colors.canvas,
    borderRadius: 14,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.border,
  },
  pickerItemCardSelected: {
    borderColor: colors.violet,
    backgroundColor: colors.violetSoft,
  },
  pickerItemBrandRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginBottom: 2,
  },
  pickerItemBrand: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.muted,
  },
  socketPill: {
    backgroundColor: colors.card,
    paddingHorizontal: 5,
    paddingVertical: 1,
    borderRadius: 4,
  },
  socketPillText: {
    fontSize: 8.5,
    fontWeight: '700',
    color: colors.ink,
  },
  pickerItemName: {
    fontSize: 13.5,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 2,
  },
  pickerItemSpecs: {
    fontSize: 11,
    color: colors.muted,
    lineHeight: 15,
  },
  pickerItemPriceColumn: {
    alignItems: 'flex-end',
    marginLeft: 12,
  },
  pickerItemPrice: {
    fontSize: 15,
    fontWeight: '900',
    color: colors.ink,
    marginBottom: 6,
  },
  pickerSelectBadge: {
    backgroundColor: colors.violet,
    paddingHorizontal: 10,
    paddingVertical: 5,
    borderRadius: 8,
  },
  pickerSelectBadgeActive: {
    backgroundColor: colors.greenLowest,
  },
  pickerSelectBadgeText: {
    fontSize: 11,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  pickerSelectBadgeTextActive: {
    color: '#FFFFFF',
  },
});
