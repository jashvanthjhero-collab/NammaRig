import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  FlatList,
  Platform,
  Alert,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import {
  Bell,
  Search,
  ChevronDown,
  Zap,
  TrendingDown,
  Layers,
  Cpu,
  MapPin,
  Sparkles,
  Clock,
  Compass,
  ArrowRight,
  Store,
  CheckCircle2,
} from 'lucide-react-native';
import { colors } from '../theme/colors';
import { useRigStore } from '../store/useRigStore';
import { formatInr } from '../lib/pricing';
import { SEED_PRODUCTS, CHENNAI_SHOPS } from '../data/mockData';
import { RootTabParamList } from '../types';

type NavigationProp = BottomTabNavigationProp<RootTabParamList>;

interface PricePulseItem {
  id: string;
  productName: string;
  category: string;
  dropAmount: number;
  currentPrice: number;
  shopName: string;
  lane: string;
  timeAgo: string;
}

const LIVE_PRICE_PULSE: PricePulseItem[] = [
  {
    id: 'pulse-1',
    productName: 'Samsung 990 Pro 2TB',
    category: 'NVMe Gen 4 SSD',
    dropAmount: 1200,
    currentPrice: 16950,
    shopName: 'Delta Peripherals',
    lane: 'Meeran Sahib St',
    timeAgo: '14m ago',
  },
  {
    id: 'pulse-2',
    productName: 'RTX 5080 16GB OC',
    category: 'Blackwell GPU',
    dropAmount: 4500,
    currentPrice: 123900,
    shopName: 'Marina Micro',
    lane: 'Mount Road',
    timeAgo: '28m ago',
  },
  {
    id: 'pulse-3',
    productName: 'G.Skill Trident Z5 32GB',
    category: 'DDR5 6000MHz',
    dropAmount: 850,
    currentPrice: 11400,
    shopName: 'Supreme Computers',
    lane: 'Wallers Road',
    timeAgo: '42m ago',
  },
  {
    id: 'pulse-4',
    productName: 'NZXT Kraken 360 RGB',
    category: 'Liquid AIO Cooler',
    dropAmount: 950,
    currentPrice: 16990,
    shopName: 'Marina Micro',
    lane: 'Mount Road',
    timeAgo: '1h ago',
  },
];

export function RigHubScreen() {
  const insets = useSafeAreaInsets();
  const navigation = useNavigation<NavigationProp>();
  const { setSlot, selectedLocation, setSelectedLocation, build } = useRigStore();
  const [addedSuccess, setAddedSuccess] = useState(false);

  const spotlightProduct = SEED_PRODUCTS.find((p) => p.id === 'cpu-9950x3d');
  const spotlightPrice = 59322;
  const spotlightSavings = 9568;
  const spotlightDealer = 'Delta Peripherals (Meeran Sahib St)';

  const isCpuEquipped = build.cpu.productId === 'cpu-9950x3d';

  const handleAddSpotlightToRig = () => {
    setSlot('cpu', 'cpu-9950x3d', 'delta-peripherals', spotlightPrice);
    setAddedSuccess(true);
    setTimeout(() => setAddedSuccess(false), 2400);
  };

  const toggleLocation = () => {
    const locations = [
      'Chennai — Ritchie Street',
      'Chennai — Mount Road',
      'Chennai — T. Nagar IT Hub',
    ];
    const currentIndex = locations.indexOf(selectedLocation);
    const nextLoc = locations[(currentIndex + 1) % locations.length];
    setSelectedLocation(nextLoc);
  };

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={[
        styles.contentContainer,
        { paddingTop: insets.top + 10, paddingBottom: 115 },
      ]}
      showsVerticalScrollIndicator={false}
    >
      {/* 1. TOP HEADER */}
      <View style={styles.topHeader}>
        <View style={styles.brandRow}>
          <View style={styles.logoBadge}>
            <Cpu size={18} color="#FFFFFF" />
          </View>
          <View>
            <View style={styles.logoTextRow}>
              <Text style={styles.brandPrefix}>NAMMA</Text>
              <Text style={styles.brandAccent}>RIG</Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6, marginTop: 1 }}>
              <TouchableOpacity
                onPress={toggleLocation}
                activeOpacity={0.7}
                style={styles.locationPill}
              >
                <MapPin size={11} color={colors.violet} />
                <Text style={styles.locationText}>{selectedLocation}</Text>
                <ChevronDown size={11} color={colors.muted} />
              </TouchableOpacity>
              <View style={styles.offlineBadge}>
                <View style={styles.offlineDot} />
                <Text style={styles.offlineText}>Offline Ready</Text>
              </View>
            </View>
          </View>
        </View>

        <View style={styles.headerActions}>
          <TouchableOpacity
            style={styles.headerIconButton}
            activeOpacity={0.75}
            onPress={() => navigation.navigate('Hunter')}
          >
            <Search size={19} color={colors.ink} />
          </TouchableOpacity>
          <TouchableOpacity
            style={styles.headerIconButton}
            activeOpacity={0.75}
            onPress={() =>
              Alert.alert(
                'Price Alerts & Notifications',
                'You are subscribed to Ritchie Street flash price drops on GPUs and high-end CPUs!'
              )
            }
          >
            <Bell size={19} color={colors.ink} />
            <View style={styles.notificationDot} />
          </TouchableOpacity>
        </View>
      </View>

      {/* 2. SPOTLIGHT HARDWARE CARD */}
      <View style={styles.spotlightContainer}>
        <View style={styles.spotlightCard}>
          {/* Header Tag */}
          <View style={styles.spotlightTagRow}>
            <View style={styles.spotlightBadge}>
              <Sparkles size={11} color={colors.violet} />
              <Text style={styles.spotlightBadgeText}>
                SPOTLIGHT HARDWARE · CHENNAI LAUNCH
              </Text>
            </View>
            <View style={styles.stockStatusBadge}>
              <View style={styles.greenPulseDot} />
              <Text style={styles.stockStatusText}>In Stock</Text>
            </View>
          </View>

          {/* Product Title & Spec Highlights */}
          <Text style={styles.spotlightProductName}>
            AMD Ryzen 9 9950X3D Processor
          </Text>
          <Text style={styles.spotlightProductSpecs}>
            16 Cores / 32 Threads • 5.7GHz Boost • 144MB 3D V-Cache • Socket AM5
          </Text>

          {/* Lowest Ritchie St Price Banner */}
          <View style={styles.priceBanner}>
            <View style={styles.priceColumn}>
              <Text style={styles.priceBannerLabel}>LOWEST RITCHIE ST DEAL</Text>
              <View style={styles.priceNumberRow}>
                <Text style={styles.spotlightPriceText}>
                  {formatInr(spotlightPrice)}
                </Text>
                <View style={styles.savePill}>
                  <Text style={styles.savePillText}>
                    Save {formatInr(spotlightSavings)}
                  </Text>
                </View>
              </View>
              <View style={styles.shopLocationRow}>
                <Store size={12} color={colors.muted} />
                <Text style={styles.shopLocationText}>at {spotlightDealer}</Text>
              </View>
            </View>
          </View>

          {/* Action Buttons */}
          <View style={styles.spotlightActionRow}>
            <TouchableOpacity
              style={[
                styles.primaryActionButton,
                (isCpuEquipped || addedSuccess) && styles.equippedButton,
              ]}
              activeOpacity={0.85}
              onPress={handleAddSpotlightToRig}
            >
              {addedSuccess || isCpuEquipped ? (
                <>
                  <CheckCircle2 size={16} color="#FFFFFF" />
                  <Text style={styles.primaryActionText}>Equipped in Rig Studio</Text>
                </>
              ) : (
                <>
                  <Zap size={16} color="#FFFFFF" fill="#FFFFFF" />
                  <Text style={styles.primaryActionText}>Add to Current Rig ⚡</Text>
                </>
              )}
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.secondaryActionButton}
              activeOpacity={0.75}
              onPress={() => navigation.navigate('Price Pulse')}
            >
              <Text style={styles.secondaryActionText}>Compare 8 Shops</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>

      {/* 3. RITCHIE ST. LIVE PRICE PULSE TICKER */}
      <View style={styles.sectionHeaderRow}>
        <View style={styles.sectionTitleBlock}>
          <View style={styles.tickerIconBadge}>
            <TrendingDown size={14} color={colors.greenLowest} />
          </View>
          <Text style={styles.sectionTitle}>Ritchie St. Live Price Pulse</Text>
        </View>
        <TouchableOpacity
          onPress={() => navigation.navigate('Price Pulse')}
          style={styles.viewAllBtn}
        >
          <Text style={styles.viewAllText}>View All</Text>
          <ArrowRight size={13} color={colors.violet} />
        </TouchableOpacity>
      </View>

      <FlatList
        data={LIVE_PRICE_PULSE}
        keyExtractor={(item) => item.id}
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.pulseListContent}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.pulseCard}
            activeOpacity={0.8}
            onPress={() => navigation.navigate('Price Pulse')}
          >
            <View style={styles.pulseCardTop}>
              <View style={styles.dropBadge}>
                <TrendingDown size={12} color={colors.greenLowest} />
                <Text style={styles.dropBadgeText}>-₹{item.dropAmount}</Text>
              </View>
              <View style={styles.timeRow}>
                <Clock size={11} color={colors.muted} />
                <Text style={styles.timeText}>{item.timeAgo}</Text>
              </View>
            </View>

            <Text style={styles.pulseProductName} numberOfLines={1}>
              {item.productName}
            </Text>
            <Text style={styles.pulseCategory}>{item.category}</Text>

            <View style={styles.pulseBottom}>
              <Text style={styles.pulseCurrentPrice}>
                {formatInr(item.currentPrice)}
              </Text>
              <View style={styles.pulseShopRow}>
                <MapPin size={11} color={colors.muted} />
                <Text style={styles.pulseShopName} numberOfLines={1}>
                  {item.shopName}
                </Text>
              </View>
            </View>
          </TouchableOpacity>
        )}
      />

      {/* 4. QUICK NAVIGATION GRID */}
      <View style={[styles.sectionHeaderRow, { marginTop: 22 }]}>
        <Text style={styles.sectionTitle}>Builder Command Center</Text>
        <Text style={styles.sectionSubtitleLabel}>3 FAST SHORTCUTS</Text>
      </View>

      <View style={styles.quickNavGrid}>
        {/* Card 1: Rig Studio CAD */}
        <TouchableOpacity
          style={styles.quickNavCard}
          activeOpacity={0.8}
          onPress={() => navigation.navigate('CAD Studio')}
        >
          <View style={[styles.navIconBox, { backgroundColor: colors.violetSoft }]}>
            <Cpu size={22} color={colors.violet} />
          </View>
          <View style={styles.navCardContent}>
            <Text style={styles.navCardTitle}>Rig Studio CAD</Text>
            <Text style={styles.navCardDesc}>
              8-Component builder with 2D blueprint schematic & socket validation.
            </Text>
          </View>
          <View style={styles.navCardFooter}>
            <Text style={[styles.navActionLabel, { color: colors.violet }]}>
              Open Studio ➔
            </Text>
          </View>
        </TouchableOpacity>

        {/* Card 2: Spec Checker */}
        <TouchableOpacity
          style={styles.quickNavCard}
          activeOpacity={0.8}
          onPress={() => navigation.navigate('Blueprint')}
        >
          <View style={[styles.navIconBox, { backgroundColor: colors.greenBg }]}>
            <Layers size={22} color={colors.greenLowest} />
          </View>
          <View style={styles.navCardContent}>
            <Text style={styles.navCardTitle}>Spec Checker</Text>
            <Text style={styles.navCardDesc}>
              TDP power headroom calculator & GPU/Cooler clearance checks.
            </Text>
          </View>
          <View style={styles.navCardFooter}>
            <View style={styles.fitPill}>
              <Text style={styles.fitPillText}>86% Fit Score</Text>
            </View>
            <Text style={[styles.navActionLabel, { color: colors.greenLowest }]}>
              Check Specs ➔
            </Text>
          </View>
        </TouchableOpacity>

        {/* Card 3: Ritchie St Transit Map */}
        <TouchableOpacity
          style={styles.quickNavCard}
          activeOpacity={0.8}
          onPress={() => navigation.navigate('Price Pulse')}
        >
          <View style={[styles.navIconBox, { backgroundColor: '#F3E8FF' }]}>
            <Compass size={22} color="#7C3AED" />
          </View>
          <View style={styles.navCardContent}>
            <Text style={styles.navCardTitle}>Ritchie St Transit Map</Text>
            <Text style={styles.navCardDesc}>
              Shortest walking loop across Meeran Sahib, Wallers & Narayana Mudali.
            </Text>
          </View>
          <View style={styles.navCardFooter}>
            <Text style={[styles.navActionLabel, { color: '#7C3AED' }]}>
              View Route (~340m) ➔
            </Text>
          </View>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: colors.canvas,
  },
  contentContainer: {
    paddingHorizontal: 16,
  },
  topHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  brandRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  logoBadge: {
    width: 38,
    height: 38,
    borderRadius: 12,
    backgroundColor: colors.violet,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: colors.violet,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.35,
    shadowRadius: 6,
    elevation: 4,
  },
  logoTextRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  brandPrefix: {
    fontSize: 20,
    fontWeight: '900',
    color: colors.ink,
    letterSpacing: -0.5,
  },
  brandAccent: {
    fontSize: 20,
    fontWeight: '900',
    color: colors.violet,
    letterSpacing: -0.5,
  },
  locationPill: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    marginTop: 1,
  },
  locationText: {
    fontSize: 11,
    color: colors.muted,
    fontWeight: '600',
  },
  headerActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  headerIconButton: {
    width: 38,
    height: 38,
    borderRadius: 19,
    backgroundColor: colors.card,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: colors.border,
  },
  notificationDot: {
    position: 'absolute',
    top: 9,
    right: 9,
    width: 7,
    height: 7,
    borderRadius: 4,
    backgroundColor: colors.violet,
  },
  spotlightContainer: {
    marginBottom: 20,
  },
  spotlightCard: {
    backgroundColor: colors.card,
    borderRadius: 22,
    padding: 18,
    borderWidth: 1,
    borderColor: colors.border,
    ...Platform.select({
      ios: {
        shadowColor: colors.ink,
        shadowOffset: { width: 0, height: 6 },
        shadowOpacity: 0.08,
        shadowRadius: 16,
      },
      android: {
        elevation: 4,
      },
    }),
  },
  spotlightTagRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  spotlightBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
    gap: 5,
  },
  spotlightBadgeText: {
    fontSize: 9.5,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 0.6,
  },
  stockStatusBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.greenBg,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 12,
    gap: 5,
  },
  greenPulseDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: colors.greenLowest,
  },
  stockStatusText: {
    fontSize: 10,
    fontWeight: '700',
    color: colors.greenLowest,
  },
  spotlightProductName: {
    fontSize: 21,
    fontWeight: '900',
    color: colors.ink,
    letterSpacing: -0.3,
    marginBottom: 4,
  },
  spotlightProductSpecs: {
    fontSize: 12.5,
    color: colors.muted,
    lineHeight: 18,
    marginBottom: 14,
  },
  priceBanner: {
    backgroundColor: colors.canvas,
    borderRadius: 14,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 14,
  },
  priceColumn: {},
  priceBannerLabel: {
    fontSize: 9.5,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.8,
    marginBottom: 4,
  },
  priceNumberRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  spotlightPriceText: {
    fontSize: 24,
    fontWeight: '900',
    color: colors.ink,
    letterSpacing: -0.5,
  },
  savePill: {
    backgroundColor: colors.greenLowest,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
  },
  savePillText: {
    fontSize: 10.5,
    fontWeight: '800',
    color: '#FFFFFF',
  },
  shopLocationRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    marginTop: 6,
  },
  shopLocationText: {
    fontSize: 11,
    color: colors.muted,
    fontWeight: '600',
  },
  spotlightActionRow: {
    flexDirection: 'row',
    gap: 10,
  },
  primaryActionButton: {
    flex: 1.2,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.violet,
    paddingVertical: 12,
    borderRadius: 12,
    gap: 6,
    shadowColor: colors.violet,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 3,
  },
  equippedButton: {
    backgroundColor: colors.greenLowest,
  },
  primaryActionText: {
    color: '#FFFFFF',
    fontSize: 12.5,
    fontWeight: '800',
  },
  secondaryActionButton: {
    flex: 0.8,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
    borderWidth: 1.5,
    borderColor: colors.border,
    paddingVertical: 12,
    borderRadius: 12,
  },
  secondaryActionText: {
    color: colors.ink,
    fontSize: 12,
    fontWeight: '700',
  },
  sectionHeaderRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  sectionTitleBlock: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  tickerIconBadge: {
    width: 22,
    height: 22,
    borderRadius: 6,
    backgroundColor: colors.greenBg,
    alignItems: 'center',
    justifyContent: 'center',
  },
  sectionTitle: {
    fontSize: 15,
    fontWeight: '800',
    color: colors.ink,
  },
  viewAllBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
  },
  viewAllText: {
    fontSize: 11.5,
    fontWeight: '700',
    color: colors.violet,
  },
  sectionSubtitleLabel: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.6,
  },
  pulseListContent: {
    gap: 10,
    paddingRight: 10,
  },
  pulseCard: {
    width: 195,
    backgroundColor: colors.card,
    borderRadius: 16,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.border,
  },
  pulseCardTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  dropBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.greenBg,
    paddingHorizontal: 6,
    paddingVertical: 2,
    borderRadius: 6,
    gap: 3,
  },
  dropBadgeText: {
    fontSize: 10.5,
    fontWeight: '800',
    color: colors.greenLowest,
  },
  timeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
  },
  timeText: {
    fontSize: 10,
    color: colors.muted,
    fontWeight: '500',
  },
  pulseProductName: {
    fontSize: 13,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 2,
  },
  pulseCategory: {
    fontSize: 10.5,
    color: colors.muted,
    marginBottom: 10,
  },
  pulseBottom: {
    borderTopWidth: 1,
    borderTopColor: colors.border,
    paddingTop: 8,
  },
  pulseCurrentPrice: {
    fontSize: 15,
    fontWeight: '900',
    color: colors.ink,
    marginBottom: 2,
  },
  pulseShopRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
  },
  pulseShopName: {
    fontSize: 10,
    color: colors.muted,
    fontWeight: '600',
  },
  quickNavGrid: {
    gap: 12,
  },
  quickNavCard: {
    backgroundColor: colors.card,
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: colors.border,
  },
  navIconBox: {
    width: 44,
    height: 44,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 10,
  },
  navCardContent: {
    marginBottom: 12,
  },
  navCardTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 4,
  },
  navCardDesc: {
    fontSize: 12,
    color: colors.muted,
    lineHeight: 17,
  },
  navCardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  fitPill: {
    backgroundColor: colors.greenBg,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    borderWidth: 1,
    borderColor: colors.greenLowest,
  },
  fitPillText: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.greenLowest,
  },
  navActionLabel: {
    fontSize: 12,
    fontWeight: '800',
  },
});
