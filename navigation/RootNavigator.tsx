import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Platform,
  ScrollView,
} from 'react-native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import {
  Home,
  Cpu,
  Layers,
  Search,
  TrendingDown,
  Sparkles,
  MapPin,
  CheckCircle2,
  Zap,
} from 'lucide-react-native';
import { colors } from '../theme/colors';
import { RootTabParamList } from '../types';
import { RigHubScreen } from '../screens/RigHubScreen';
import { ComponentHunterScreen } from '../screens/ComponentHunterScreen';
import { RigStudioScreen } from '../screens/RigStudioScreen';
import { SpecCheckerScreen } from '../screens/SpecCheckerScreen';
import { ShoppingSlipScreen } from '../screens/ShoppingSlipScreen';

const Tab = createBottomTabNavigator<RootTabParamList>();

// --- Custom Frosted Floating Tab Bar ---

interface TabBarProps {
  state: any;
  descriptors: any;
  navigation: any;
}

function CustomTabBar({ state, descriptors, navigation }: TabBarProps) {
  const insets = useSafeAreaInsets();

  return (
    <View style={[styles.tabBarWrapper, { bottom: Math.max(insets.bottom, 12) }]}>
      <View style={styles.tabBarContainer}>
        {state.routes.map((route: any, index: number) => {
          const { options } = descriptors[route.key];
          const isFocused = state.index === index;
          const isCenter = route.name === 'Blueprint';

          const onPress = () => {
            const event = navigation.emit({
              type: 'tabPress',
              target: route.key,
              canPreventDefault: true,
            });

            if (!isFocused && !event.defaultPrevented) {
              navigation.navigate(route.name);
            }
          };

          if (isCenter) {
            return (
              <TouchableOpacity
                key={route.key}
                onPress={onPress}
                activeOpacity={0.85}
                style={styles.centerTabWrapper}
              >
                {/* 86% Fit Badge over center button */}
                <View style={styles.centerPillBadge}>
                  <Text style={styles.centerPillText}>86% Fit</Text>
                </View>

                <View
                  style={[
                    styles.centerButton,
                    isFocused && styles.centerButtonActive,
                  ]}
                >
                  <Layers size={22} color="#FFFFFF" strokeWidth={2.3} />
                </View>
                <Text
                  style={[
                    styles.tabLabel,
                    { color: isFocused ? colors.violet : colors.muted },
                  ]}
                >
                  Blueprint
                </Text>
              </TouchableOpacity>
            );
          }

          const renderIcon = () => {
            const color = isFocused ? colors.violet : colors.muted;
            const size = 20;
            switch (route.name) {
              case 'Rig Hub':
                return <Home size={size} color={color} strokeWidth={isFocused ? 2.4 : 1.8} />;
              case 'CAD Studio':
                return <Cpu size={size} color={color} strokeWidth={isFocused ? 2.4 : 1.8} />;
              case 'Hunter':
                return <Search size={size} color={color} strokeWidth={isFocused ? 2.4 : 1.8} />;
              case 'Price Pulse':
                return (
                  <TrendingDown size={size} color={color} strokeWidth={isFocused ? 2.4 : 1.8} />
                );
              default:
                return null;
            }
          };

          return (
            <TouchableOpacity
              key={route.key}
              onPress={onPress}
              activeOpacity={0.7}
              style={styles.tabItem}
            >
              <View style={[styles.iconWrapper, isFocused && styles.iconWrapperActive]}>
                {renderIcon()}
              </View>
              <Text
                style={[
                  styles.tabLabel,
                  { color: isFocused ? colors.violet : colors.muted },
                ]}
                numberOfLines={1}
              >
                {route.name}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>
    </View>
  );
}

export function RootNavigator() {
  return (
    <Tab.Navigator
      tabBar={(props) => <CustomTabBar {...props} />}
      screenOptions={{
        headerShown: false,
      }}
    >
      <Tab.Screen name="Rig Hub" component={RigHubScreen} />
      <Tab.Screen name="CAD Studio" component={RigStudioScreen} />
      <Tab.Screen name="Blueprint" component={SpecCheckerScreen} />
      <Tab.Screen name="Hunter" component={ComponentHunterScreen} />
      <Tab.Screen name="Price Pulse" component={ShoppingSlipScreen} />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  tabBarWrapper: {
    position: 'absolute',
    left: 14,
    right: 14,
    zIndex: 99,
  },
  tabBarContainer: {
    flexDirection: 'row',
    backgroundColor: '#FFFFFF',
    borderRadius: 24,
    paddingVertical: 8,
    paddingHorizontal: 8,
    alignItems: 'center',
    justifyContent: 'space-around',
    borderWidth: 1,
    borderColor: colors.border,
    ...Platform.select({
      ios: {
        shadowColor: '#090D16',
        shadowOffset: { width: 0, height: 8 },
        shadowOpacity: 0.1,
        shadowRadius: 18,
      },
      android: {
        elevation: 10,
      },
    }),
  },
  tabItem: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 4,
  },
  iconWrapper: {
    padding: 4,
    borderRadius: 12,
  },
  iconWrapperActive: {
    backgroundColor: colors.violetSoft,
  },
  tabLabel: {
    fontSize: 10,
    fontWeight: '700',
    marginTop: 2,
    letterSpacing: 0.2,
  },
  centerTabWrapper: {
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: -22,
    flex: 1,
  },
  centerPillBadge: {
    backgroundColor: colors.greenLowest,
    paddingHorizontal: 7,
    paddingVertical: 2,
    borderRadius: 10,
    marginBottom: 3,
    borderWidth: 1.5,
    borderColor: '#FFFFFF',
  },
  centerPillText: {
    color: '#FFFFFF',
    fontSize: 8.5,
    fontWeight: '900',
    letterSpacing: 0.3,
  },
  centerButton: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: colors.violet,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 3,
    borderColor: '#FFFFFF',
    shadowColor: colors.violet,
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.45,
    shadowRadius: 10,
    elevation: 8,
  },
  centerButtonActive: {
    backgroundColor: '#5234D4',
  },
  screenContainer: {
    flex: 1,
    backgroundColor: colors.canvas,
  },
  screenContent: {
    paddingHorizontal: 18,
    paddingTop: 54,
    paddingBottom: 110,
  },
  header: {
    marginBottom: 16,
  },
  tagline: {
    fontSize: 11,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 1.2,
    marginBottom: 4,
  },
  title: {
    fontSize: 26,
    fontWeight: '900',
    color: colors.ink,
  },
  subtitle: {
    fontSize: 13,
    color: colors.muted,
    marginTop: 2,
  },
  heroCard: {
    backgroundColor: colors.card,
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: colors.border,
    shadowColor: colors.ink,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.05,
    shadowRadius: 10,
    elevation: 3,
  },
  heroBadgeRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.violetSoft,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    gap: 4,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.violet,
    letterSpacing: 0.5,
  },
  priceTag: {
    fontSize: 16,
    fontWeight: '900',
    color: colors.greenLowest,
  },
  heroCardTitle: {
    fontSize: 18,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 4,
  },
  heroCardSubtitle: {
    fontSize: 12.5,
    color: colors.muted,
    lineHeight: 18,
  },
  dealerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 10,
    gap: 4,
  },
  dealerText: {
    fontSize: 11.5,
    color: colors.muted,
    fontWeight: '500',
  },
  fitBadge: {
    backgroundColor: colors.greenBg,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: colors.greenLowest,
  },
  fitBadgeText: {
    fontSize: 11,
    fontWeight: '800',
    color: colors.greenLowest,
  },
});
