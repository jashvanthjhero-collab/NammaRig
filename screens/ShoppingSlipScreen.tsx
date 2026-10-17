import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Linking,
  Share,
  Platform,
  Alert,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import {
  MapPin,
  Directions,
  Compass,
  MessageCircle,
  Share2,
  CheckSquare,
  Square,
  ShieldAlert,
  ShieldCheck,
  Receipt,
  Store,
  Clock,
  Sparkles,
  ExternalLink,
  Info,
} from 'lucide-react-native';
import { colors } from '../theme/colors';
import { useRigStore } from '../store/useRigStore';
import { formatInr } from '../lib/pricing';
import {
  CHENNAI_SHOPS,
  SEED_PRODUCTS,
  getShopById,
  getProductById,
} from '../data/mockData';
import { BuildSlotCategory, Product, RootTabParamList, Shop } from '../types';

type NavigationProp = BottomTabNavigationProp<RootTabParamList>;

interface CheckedItemsMap {
  [productId: string]: boolean;
}

interface GroupedShopStop {
  shop: Shop;
  stopNumber: number;
  items: {
    category: BuildSlotCategory;
    product: Product;
    price: number;
  }[];
  subtotal: number;
}

export function ShoppingSlipScreen() {
  const insets = useSafeAreaInsets();
  const navigation = useNavigation<NavigationProp>();
  const { build, loadSeedPreset, getTotalPrice } = useRigStore();

  const [checkedItems, setCheckedItems] = useState<CheckedItemsMap>({});

  const toggleCheckItem = (productId: string) => {
    setCheckedItems((prev) => ({
      ...prev,
      [productId]: !prev[productId],
    }));
  };

  // Group build slots by shop
  const groupedStops: GroupedShopStop[] = React.useMemo(() => {
    const stopsMap: { [shopId: string]: GroupedShopStop } = {};

    const categories = Object.keys(build) as BuildSlotCategory[];
    categories.forEach((cat) => {
      const slot = build[cat];
      if (slot?.productId && slot?.shopId) {
        const product = getProductById(slot.productId);
        const shop = getShopById(slot.shopId);
        if (product && shop) {
          if (!stopsMap[shop.id]) {
            stopsMap[shop.id] = {
              shop,
              stopNumber: Object.keys(stopsMap).length + 1,
              items: [],
              subtotal: 0,
            };
          }
          stopsMap[shop.id].items.push({
            category: cat,
            product,
            price: slot.price || 0,
          });
          stopsMap[shop.id].subtotal += slot.price || 0;
        }
      }
    });

    return Object.values(stopsMap);
  }, [build]);

  const totalBuildPrice = getTotalPrice();
  const totalItemCount = groupedStops.reduce((sum, s) => sum + s.items.length, 0);

  // Generate WhatsApp inquiry text
  const handleOpenWhatsApp = (shop: Shop, items: GroupedShopStop['items'], subtotal: number) => {
    const cleanNumber = shop.whatsappNumber.replace(/\D/g, '');
    let text = `Vanakkam ${shop.name}! 👋\nInquiring from NammaRig App (Chennai):\n`;
    text += `I am planning to visit your ${shop.area} shop at ${shop.landmark} today to buy:\n\n`;

    items.forEach((item, idx) => {
      text += `${idx + 1}. ${item.product.name} — ${formatInr(item.price)}\n`;
    });

    text += `\nTotal Estimate: ${formatInr(subtotal)} (18% GST invoice)\n`;
    text += `Can you please confirm current stock and bench-testing availability?\nNandri!`;

    const encodedText = encodeURIComponent(text);
    const url = `https://api.whatsapp.com/send?phone=${cleanNumber}&text=${encodedText}`;

    Linking.canOpenURL(url)
      .then((supported) => {
        if (supported) {
          Linking.openURL(url);
        } else {
          Alert.alert('WhatsApp Not Installed', `Please contact ${shop.name} at ${shop.phone || shop.whatsappNumber}`);
        }
      })
      .catch(() => {
        Linking.openURL(url);
      });
  };

  // Open Google Maps directions
  const handleOpenDirections = (shop: Shop) => {
    const query = encodeURIComponent(`${shop.name}, ${shop.landmark}, ${shop.area}, Chennai`);
    const mapsUrl = `https://www.google.com/maps/search/?api=1&query=${query}`;
    Linking.openURL(mapsUrl);
  };

  // Native Share functionality
  const handleShareShoppingSlip = async () => {
    if (totalItemCount === 0) {
      Alert.alert('Empty Build', 'Please add components to your rig before sharing the shopping slip.');
      return;
    }

    let shareText = `🛒 NammaRig — Ritchie Street Official Shopping Slip\n`;
    shareText += `=========================================\n`;
    shareText += `Total Estimate: ${formatInr(totalBuildPrice)}\n`;
    shareText += `Route: Meeran Sahib St ➔ Wallers Rd ➔ Ritchie Main Lane\n`;
    shareText += `Stops: ${groupedStops.length} Dealer Stops (~380m total walk)\n\n`;

    groupedStops.forEach((stop) => {
      shareText += `📍 STOP ${stop.stopNumber}: ${stop.shop.name}\n`;
      shareText += `   Address: ${stop.shop.landmark}, ${stop.shop.area}\n`;
      shareText += `   Contact: ${stop.shop.whatsappNumber}\n`;
      stop.items.forEach((item) => {
        shareText += `   [ ] ${item.product.name} — ${formatInr(item.price)}\n`;
      });
      shareText += `   Subtotal: ${formatInr(stop.subtotal)}\n\n`;
    });

    shareText += `Built with NammaRig — Chennai's PC & Laptop Hub`;

    try {
      await Share.share({
        message: shareText,
        title: 'Ritchie Street Shopping Slip',
      });
    } catch (error) {
      console.log('Share error:', error);
    }
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
      {/* 1. TOP HEADER & SHARE BUTTON */}
      <View style={styles.topHeader}>
        <View>
          <Text style={styles.headerTagline}>RITCHIE STREET WALKING GUIDE</Text>
          <Text style={styles.headerTitle}>Shopping Slip</Text>
        </View>

        <TouchableOpacity
          style={styles.shareIconButton}
          activeOpacity={0.75}
          onPress={handleShareShoppingSlip}
        >
          <Share2 size={18} color={colors.violet} />
          <Text style={styles.shareIconButtonText}>Share Slip</Text>
        </TouchableOpacity>
      </View>

      {/* 2. ROUTE WALK SUMMARY CARD */}
      <View style={styles.routeSummaryCard}>
        <View style={styles.routeTopRow}>
          <View style={styles.stopsBadge}>
            <Compass size={13} color={colors.violet} />
            <Text style={styles.stopsBadgeText}>
              {groupedStops.length} STOPS · ~380m TOTAL WALK (~12 MIN)
            </Text>
          </View>
          <Text style={styles.totalQuoteAmount}>
            {formatInr(totalBuildPrice)}
          </Text>
        </View>

        {/* Route Path Sequence */}
        <View style={styles.routePathContainer}>
          <Text style={styles.routePathLabel}>OPTIMIZED WALKING SEQUENCE:</Text>
          <Text style={styles.routePathText}>
            Meeran Sahib St ➔ Wallers Rd ➔ Ritchie Main Lane
          </Text>
        </View>

        <View style={styles.itemCountFooter}>
          <Text style={styles.itemCountText}>
            {totalItemCount} components grouped across {groupedStops.length} verified dealers
          </Text>
        </View>
      </View>

      {/* Empty State / Preset Helper */}
      {groupedStops.length === 0 && (
        <View style={styles.emptyCard}>
          <Receipt size={36} color={colors.muted} />
          <Text style={styles.emptyCardTitle}>No Components in Current Rig</Text>
          <Text style={styles.emptyCardDesc}>
            Equip components from Rig Studio CAD or load the recommended high-end build.
          </Text>
          <TouchableOpacity
            style={styles.loadPresetBtn}
            onPress={loadSeedPreset}
            activeOpacity={0.8}
          >
            <Sparkles size={14} color="#FFFFFF" />
            <Text style={styles.loadPresetBtnText}>Load 8-Part Flagship Rig</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* 3. GROUPED SHOPS STOP-BY-STOP */}
      {groupedStops.map((stop) => {
        return (
          <View key={stop.shop.id} style={styles.stopCard}>
            {/* Stop Header */}
            <View style={styles.stopHeaderRow}>
              <View style={styles.stopBadge}>
                <Text style={styles.stopBadgeNumber}>STOP {stop.stopNumber}</Text>
              </View>
              <View style={styles.stopTitleBlock}>
                <Text style={styles.stopShopName}>{stop.shop.name}</Text>
                <View style={styles.stopLandmarkRow}>
                  <MapPin size={11} color={colors.muted} />
                  <Text style={styles.stopLandmarkText}>
                    {stop.shop.landmark} • {stop.shop.area}
                  </Text>
                </View>
              </View>

              <Text style={styles.stopSubtotal}>
                {formatInr(stop.subtotal)}
              </Text>
            </View>

            {/* Itemized Parts Checklist */}
            <View style={styles.partsChecklist}>
              {stop.items.map((item) => {
                const isChecked = !!checkedItems[item.product.id];

                return (
                  <TouchableOpacity
                    key={item.product.id}
                    style={[
                      styles.checklistItemRow,
                      isChecked && styles.checklistItemRowChecked,
                    ]}
                    activeOpacity={0.7}
                    onPress={() => toggleCheckItem(item.product.id)}
                  >
                    {isChecked ? (
                      <CheckSquare size={18} color={colors.greenLowest} />
                    ) : (
                      <Square size={18} color={colors.muted} />
                    )}

                    <View style={styles.checklistItemInfo}>
                      <Text
                        style={[
                          styles.checklistItemName,
                          isChecked && styles.checklistItemNameChecked,
                        ]}
                        numberOfLines={1}
                      >
                        {item.product.name}
                      </Text>
                      <Text style={styles.checklistItemCategory}>
                        {item.category.toUpperCase()} • 18% GST INVOICE
                      </Text>
                    </View>

                    <Text
                      style={[
                        styles.checklistItemPrice,
                        isChecked && styles.checklistItemPriceChecked,
                      ]}
                    >
                      {formatInr(item.price)}
                    </Text>
                  </TouchableOpacity>
                );
              })}
            </View>

            {/* Action Buttons: WhatsApp Shop & Directions */}
            <View style={styles.stopActionsRow}>
              <TouchableOpacity
                style={styles.whatsappButton}
                activeOpacity={0.8}
                onPress={() => handleOpenWhatsApp(stop.shop, stop.items, stop.subtotal)}
              >
                <MessageCircle size={15} color="#FFFFFF" />
                <Text style={styles.whatsappButtonText}>WhatsApp Shop</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.directionsButton}
                activeOpacity={0.8}
                onPress={() => handleOpenDirections(stop.shop)}
              >
                <Directions size={15} color={colors.ink} />
                <Text style={styles.directionsButtonText}>Walking Directions</Text>
              </TouchableOpacity>
            </View>
          </View>
        );
      })}

      {/* 4. RITCHIE STREET BUYER PROTECTION TIPS */}
      <View style={styles.buyerProtectionCard}>
        <View style={styles.tipsHeaderRow}>
          <ShieldAlert size={18} color={colors.violet} />
          <Text style={styles.tipsHeaderTitle}>
            Ritchie Street Buyer Protection Tips
          </Text>
        </View>

        <View style={styles.tipItemRow}>
          <View style={styles.tipDot} />
          <Text style={styles.tipText}>
            <Text style={styles.tipBold}>Verify brand hologram seal:</Text> Always inspect box tamper tape and serial stickers before billing.
          </Text>
        </View>

        <View style={styles.tipItemRow}>
          <View style={styles.tipDot} />
          <Text style={styles.tipText}>
            <Text style={styles.tipBold}>Demand official 18% GST tax invoice:</Text> Ensure GSTIN and component serial numbers are printed for warranty claims.
          </Text>
        </View>

        <View style={styles.tipItemRow}>
          <View style={styles.tipDot} />
          <Text style={styles.tipText}>
            <Text style={styles.tipBold}>Test parts on shop bench before leaving:</Text> Most dealers will gladly boot and POST the motherboard + CPU for peace of mind.
          </Text>
        </View>
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
    marginBottom: 14,
  },
  headerTagline: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 1.1,
    marginBottom: 2,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: '900',
    color: colors.ink,
    letterSpacing: -0.4,
  },
  shareIconButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.card,
    borderWidth: 1,
    borderColor: colors.border,
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 10,
    gap: 5,
  },
  shareIconButtonText: {
    fontSize: 12,
    fontWeight: '700',
    color: colors.violet,
  },
  routeSummaryCard: {
    backgroundColor: colors.card,
    borderRadius: 20,
    padding: 16,
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 16,
    ...Platform.select({
      ios: {
        shadowColor: colors.ink,
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.05,
        shadowRadius: 10,
      },
      android: {
        elevation: 3,
      },
    }),
  },
  routeTopRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  stopsBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 6,
    gap: 5,
  },
  stopsBadgeText: {
    fontSize: 9.5,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 0.5,
  },
  totalQuoteAmount: {
    fontSize: 19,
    fontWeight: '900',
    color: colors.greenLowest,
  },
  routePathContainer: {
    backgroundColor: colors.canvas,
    borderRadius: 12,
    padding: 10,
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 10,
  },
  routePathLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.8,
    marginBottom: 2,
  },
  routePathText: {
    fontSize: 12.5,
    fontWeight: '800',
    color: colors.ink,
  },
  itemCountFooter: {
    paddingTop: 2,
  },
  itemCountText: {
    fontSize: 11,
    color: colors.muted,
    fontWeight: '500',
  },
  emptyCard: {
    backgroundColor: colors.card,
    borderRadius: 18,
    padding: 24,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 16,
  },
  emptyCardTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: colors.ink,
    marginTop: 10,
    marginBottom: 4,
  },
  emptyCardDesc: {
    fontSize: 12,
    color: colors.muted,
    textAlign: 'center',
    marginBottom: 14,
    lineHeight: 17,
  },
  loadPresetBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violet,
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderRadius: 10,
    gap: 6,
  },
  loadPresetBtnText: {
    color: '#FFFFFF',
    fontSize: 12.5,
    fontWeight: '800',
  },
  stopCard: {
    backgroundColor: colors.card,
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 14,
  },
  stopHeaderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  stopBadge: {
    backgroundColor: colors.violet,
    paddingHorizontal: 7,
    paddingVertical: 3,
    borderRadius: 6,
    marginRight: 10,
  },
  stopBadgeNumber: {
    color: '#FFFFFF',
    fontSize: 9.5,
    fontWeight: '900',
    letterSpacing: 0.5,
  },
  stopTitleBlock: {
    flex: 1,
  },
  stopShopName: {
    fontSize: 15,
    fontWeight: '800',
    color: colors.ink,
  },
  stopLandmarkRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
    marginTop: 1,
  },
  stopLandmarkText: {
    fontSize: 11,
    color: colors.muted,
    fontWeight: '500',
  },
  stopSubtotal: {
    fontSize: 15,
    fontWeight: '900',
    color: colors.ink,
  },
  partsChecklist: {
    gap: 8,
    marginBottom: 14,
  },
  checklistItemRow: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.canvas,
    borderRadius: 12,
    paddingVertical: 8,
    paddingHorizontal: 10,
    borderWidth: 1,
    borderColor: colors.border,
    gap: 10,
  },
  checklistItemRowChecked: {
    backgroundColor: colors.greenBg,
    borderColor: colors.greenLowest,
  },
  checklistItemInfo: {
    flex: 1,
  },
  checklistItemName: {
    fontSize: 13,
    fontWeight: '700',
    color: colors.ink,
  },
  checklistItemNameChecked: {
    textDecorationLine: 'line-through',
    color: colors.muted,
  },
  checklistItemCategory: {
    fontSize: 9.5,
    fontWeight: '600',
    color: colors.muted,
    marginTop: 1,
  },
  checklistItemPrice: {
    fontSize: 13,
    fontWeight: '800',
    color: colors.ink,
  },
  checklistItemPriceChecked: {
    color: colors.muted,
  },
  stopActionsRow: {
    flexDirection: 'row',
    gap: 8,
  },
  whatsappButton: {
    flex: 1.1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.greenLowest,
    paddingVertical: 10,
    borderRadius: 10,
    gap: 6,
  },
  whatsappButtonText: {
    color: '#FFFFFF',
    fontSize: 12,
    fontWeight: '800',
  },
  directionsButton: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
    borderWidth: 1.5,
    borderColor: colors.border,
    paddingVertical: 10,
    borderRadius: 10,
    gap: 5,
  },
  directionsButtonText: {
    color: colors.ink,
    fontSize: 11.5,
    fontWeight: '700',
  },
  buyerProtectionCard: {
    backgroundColor: '#FAF5FF',
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: '#E9D5FF',
    marginBottom: 20,
  },
  tipsHeaderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginBottom: 10,
  },
  tipsHeaderTitle: {
    fontSize: 14,
    fontWeight: '800',
    color: colors.violet,
  },
  tipItemRow: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 8,
    marginBottom: 8,
  },
  tipDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: colors.violet,
    marginTop: 6,
  },
  tipText: {
    flex: 1,
    fontSize: 11.5,
    color: colors.ink,
    lineHeight: 16.5,
  },
  tipBold: {
    fontWeight: '800',
    color: colors.ink,
  },
});
