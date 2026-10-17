import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Platform,
  Alert,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useNavigation } from '@react-navigation/native';
import { BottomTabNavigationProp } from '@react-navigation/bottom-tabs';
import {
  ShieldCheck,
  CheckCircle2,
  AlertTriangle,
  Zap,
  Wand2,
  ArrowRight,
  Layers,
  Cpu,
  Fan,
  Box as BoxIcon,
  HardDrive,
  Sparkles,
} from 'lucide-react-native';
import { colors } from '../theme/colors';
import { useRigStore } from '../store/useRigStore';
import { formatInr } from '../lib/pricing';
import { RootTabParamList } from '../types';

type NavigationProp = BottomTabNavigationProp<RootTabParamList>;

interface DiagnosticRule {
  id: string;
  title: string;
  status: 'PASSED' | 'ADVISORY' | 'CRITICAL';
  description: string;
  componentMeta: string;
}

export function SpecCheckerScreen() {
  const insets = useSafeAreaInsets();
  const navigation = useNavigation<NavigationProp>();
  const { build, getFilledCount } = useRigStore();

  const [isAutoFixed, setIsAutoFixed] = useState(false);
  const [fitPercentage, setFitPercentage] = useState(94);

  // Electrical Power Gauge numbers
  const estimatedDrawWatts = 680;
  const psuCapacityWatts = 850;
  const headroomWatts = psuCapacityWatts - estimatedDrawWatts; // 170W Headroom

  const rules: DiagnosticRule[] = [
    {
      id: 'rule-socket',
      title: 'AM5 Socket & BIOS Compatibility',
      status: 'PASSED',
      description:
        'Ryzen 9 9950X3D and MSI X870E Tomahawk share AM5 LGA1718 pinout. AGESA 1.2.0.2 ready out of the box.',
      componentMeta: 'CPU ↔ Motherboard',
    },
    {
      id: 'rule-pcie',
      title: 'PCIe Gen 5.0 x16 & M.2 Lane Clearance',
      status: 'PASSED',
      description:
        'RTX 5080 operates at full x16 bandwidth without bifurcating the primary Gen 5 M.2 NVMe slot.',
      componentMeta: 'GPU ↔ Storage Lanes',
    },
    {
      id: 'rule-expo',
      title: 'DDR5 AMD EXPO 6000MT/s Profile 1',
      status: isAutoFixed ? 'PASSED' : 'ADVISORY',
      description: isAutoFixed
        ? 'Optimal 1:1 FCLK infinity fabric ratio locked at 2000MHz. Sub-timings tightened for 144MB 3D V-Cache.'
        : 'Enable AMD EXPO in UEFI to guarantee rated CL30 sub-timings. Default JEDEC falls back to 4800MHz.',
      componentMeta: 'RAM Memory Profile',
    },
    {
      id: 'rule-chassis',
      title: 'Chassis Clearance for 360mm Radiator',
      status: 'PASSED',
      description:
        'Lian Li O11 EVO supports top or side mount for NZXT Kraken 360mm (394mm rad length vs 420mm tray).',
      componentMeta: 'Cooler ↔ Cabinet Fitment',
    },
  ];

  const handleAutoFix = () => {
    setIsAutoFixed(true);
    setFitPercentage(100);
    Alert.alert(
      'Rig Auto-Tuned! 🚀',
      'EXPO Profile 1 sub-timings enabled (CL30-36-36-96 @ 6000MT/s). Compatibility is now 100% PERFECT FIT.'
    );
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
        <View>
          <Text style={styles.headerTagline}>DIAGNOSTIC BLUEPRINT ENGINE</Text>
          <Text style={styles.headerTitle}>Spec Checker</Text>
        </View>

        <View style={styles.chassisCodeBadge}>
          <Text style={styles.chassisCodeText}>CHENNAI LAB TESTED</Text>
        </View>
      </View>

      {/* 2. CIRCULAR COMPATIBILITY RADIAL METER CARD */}
      <View style={styles.radialCard}>
        <View style={styles.radialMetricRow}>
          {/* Radial Circular Meter Display */}
          <View style={styles.radialMeterWrapper}>
            <View style={styles.radialOuterRing}>
              <View style={styles.radialInnerRing}>
                <Text style={styles.radialScoreText}>{fitPercentage}%</Text>
                <Text style={styles.radialScoreLabel}>FIT SCORE</Text>
              </View>
            </View>
          </View>

          {/* Radial Verdict Text */}
          <View style={styles.radialVerdictColumn}>
            <View style={styles.fitStatusPill}>
              <CheckCircle2 size={12} color={colors.greenLowest} />
              <Text style={styles.fitStatusPillText}>
                {isAutoFixed ? '100% PERFECT FIT' : 'OPTIMAL FIT'}
              </Text>
            </View>
            <Text style={styles.radialVerdictTitle}>
              Physical & Power Verified
            </Text>
            <Text style={styles.radialVerdictDesc}>
              No socket conflicts or dimensional collisions detected inside the Lian Li chassis.
            </Text>
          </View>
        </View>
      </View>

      {/* 3. REAL-TIME ELECTRICAL POWER GAUGE */}
      <View style={styles.powerGaugeCard}>
        <View style={styles.powerHeaderRow}>
          <View style={styles.powerTitleGroup}>
            <View style={styles.powerIconBadge}>
              <Zap size={14} color={colors.violet} />
            </View>
            <Text style={styles.powerCardTitle}>TDP Electrical Headroom</Text>
          </View>
          <Text style={styles.powerNumbers}>
            {estimatedDrawWatts}W / {psuCapacityWatts}W
          </Text>
        </View>

        {/* Progress Bar Gauge */}
        <View style={styles.powerProgressBarBg}>
          <View
            style={[
              styles.powerProgressBarFill,
              { width: `${(estimatedDrawWatts / psuCapacityWatts) * 100}%` },
            ]}
          />
        </View>

        <View style={styles.powerFooterRow}>
          <View style={styles.headroomBadge}>
            <Text style={styles.headroomBadgeText}>
              +{headroomWatts}W Safe Buffer (20% Headroom)
            </Text>
          </View>
          <Text style={styles.psuTierText}>Corsair 850W Gold</Text>
        </View>
      </View>

      {/* 4. DIAGNOSTIC RULE CHECK CARDS */}
      <View style={styles.rulesSectionHeader}>
        <Text style={styles.rulesSectionTitle}>Diagnostic Clearance Rules</Text>
        <Text style={styles.rulesSectionCount}>
          {rules.filter((r) => r.status === 'PASSED').length}/4 PASSED
        </Text>
      </View>

      <View style={styles.rulesList}>
        {rules.map((rule) => {
          const isPassed = rule.status === 'PASSED';

          return (
            <View
              key={rule.id}
              style={[
                styles.ruleCard,
                !isPassed && styles.ruleCardAdvisory,
              ]}
            >
              <View style={styles.ruleCardHeader}>
                <View style={styles.ruleStatusGroup}>
                  {isPassed ? (
                    <CheckCircle2 size={16} color={colors.greenLowest} />
                  ) : (
                    <AlertTriangle size={16} color="#D97706" />
                  )}
                  <Text style={styles.ruleTitle}>{rule.title}</Text>
                </View>

                <View
                  style={[
                    styles.statusPill,
                    isPassed ? styles.statusPillPassed : styles.statusPillAdvisory,
                  ]}
                >
                  <Text
                    style={[
                      styles.statusPillText,
                      isPassed ? styles.statusPillTextPassed : styles.statusPillTextAdvisory,
                    ]}
                  >
                    {rule.status}
                  </Text>
                </View>
              </View>

              <Text style={styles.ruleDescription}>{rule.description}</Text>

              <View style={styles.ruleCardFooter}>
                <Text style={styles.componentMetaText}>{rule.componentMeta}</Text>
              </View>
            </View>
          );
        })}
      </View>

      {/* 5. 1-CLICK AUTO-FIX RIG BUTTON */}
      <TouchableOpacity
        style={[
          styles.autoFixButton,
          isAutoFixed && styles.autoFixButtonDone,
        ]}
        activeOpacity={0.85}
        onPress={handleAutoFix}
        disabled={isAutoFixed}
      >
        <Wand2 size={18} color="#FFFFFF" />
        <Text style={styles.autoFixButtonText}>
          {isAutoFixed ? 'Rig Fully Optimized (100%)' : '1-Click Auto-Fix Rig'}
        </Text>
      </TouchableOpacity>
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
  chassisCodeBadge: {
    backgroundColor: colors.card,
    borderWidth: 1,
    borderColor: colors.border,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  chassisCodeText: {
    fontSize: 9.5,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.5,
  },
  radialCard: {
    backgroundColor: colors.card,
    borderRadius: 22,
    padding: 18,
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 14,
    ...Platform.select({
      ios: {
        shadowColor: colors.ink,
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.06,
        shadowRadius: 12,
      },
      android: {
        elevation: 3,
      },
    }),
  },
  radialMetricRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 16,
  },
  radialMeterWrapper: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  radialOuterRing: {
    width: 90,
    height: 90,
    borderRadius: 45,
    backgroundColor: colors.greenBg,
    borderWidth: 5,
    borderColor: colors.greenLowest,
    alignItems: 'center',
    justifyContent: 'center',
  },
  radialInnerRing: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  radialScoreText: {
    fontSize: 22,
    fontWeight: '900',
    color: colors.greenLowest,
    letterSpacing: -0.5,
  },
  radialScoreLabel: {
    fontSize: 8,
    fontWeight: '800',
    color: colors.greenLowest,
    letterSpacing: 0.5,
  },
  radialVerdictColumn: {
    flex: 1,
  },
  fitStatusPill: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: colors.greenBg,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
    gap: 4,
    alignSelf: 'flex-start',
    marginBottom: 6,
  },
  fitStatusPillText: {
    fontSize: 10,
    fontWeight: '800',
    color: colors.greenLowest,
    letterSpacing: 0.5,
  },
  radialVerdictTitle: {
    fontSize: 15,
    fontWeight: '800',
    color: colors.ink,
    marginBottom: 3,
  },
  radialVerdictDesc: {
    fontSize: 11.5,
    color: colors.muted,
    lineHeight: 16,
  },
  powerGaugeCard: {
    backgroundColor: colors.card,
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: colors.border,
    marginBottom: 18,
  },
  powerHeaderRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  powerTitleGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  powerIconBadge: {
    width: 24,
    height: 24,
    borderRadius: 6,
    backgroundColor: colors.violetSoft,
    alignItems: 'center',
    justifyContent: 'center',
  },
  powerCardTitle: {
    fontSize: 13.5,
    fontWeight: '800',
    color: colors.ink,
  },
  powerNumbers: {
    fontSize: 13.5,
    fontWeight: '900',
    color: colors.ink,
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
  },
  powerProgressBarBg: {
    height: 10,
    borderRadius: 5,
    backgroundColor: colors.canvas,
    overflow: 'hidden',
    marginBottom: 10,
  },
  powerProgressBarFill: {
    height: '100%',
    borderRadius: 5,
    backgroundColor: colors.violet,
  },
  powerFooterRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  headroomBadge: {
    backgroundColor: colors.greenBg,
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
  },
  headroomBadgeText: {
    fontSize: 10.5,
    fontWeight: '700',
    color: colors.greenLowest,
  },
  psuTierText: {
    fontSize: 11,
    fontWeight: '600',
    color: colors.muted,
  },
  rulesSectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  rulesSectionTitle: {
    fontSize: 15,
    fontWeight: '800',
    color: colors.ink,
  },
  rulesSectionCount: {
    fontSize: 11,
    fontWeight: '800',
    color: colors.muted,
    letterSpacing: 0.6,
  },
  rulesList: {
    gap: 10,
    marginBottom: 18,
  },
  ruleCard: {
    backgroundColor: colors.card,
    borderRadius: 16,
    padding: 14,
    borderWidth: 1,
    borderColor: colors.border,
  },
  ruleCardAdvisory: {
    borderColor: '#FDE68A',
    backgroundColor: '#FFFDF5',
  },
  ruleCardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  ruleStatusGroup: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    flex: 1,
  },
  ruleTitle: {
    fontSize: 13.5,
    fontWeight: '800',
    color: colors.ink,
    flex: 1,
  },
  statusPill: {
    paddingHorizontal: 7,
    paddingVertical: 2.5,
    borderRadius: 5,
  },
  statusPillPassed: {
    backgroundColor: colors.greenBg,
  },
  statusPillAdvisory: {
    backgroundColor: '#FEF3C7',
  },
  statusPillText: {
    fontSize: 9.5,
    fontWeight: '900',
  },
  statusPillTextPassed: {
    color: colors.greenLowest,
  },
  statusPillTextAdvisory: {
    color: '#D97706',
  },
  ruleDescription: {
    fontSize: 12,
    color: colors.muted,
    lineHeight: 17,
    marginBottom: 8,
  },
  ruleCardFooter: {
    borderTopWidth: 1,
    borderTopColor: colors.border,
    paddingTop: 6,
  },
  componentMetaText: {
    fontSize: 10,
    fontWeight: '700',
    color: colors.muted,
    letterSpacing: 0.3,
  },
  autoFixButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: colors.violet,
    borderRadius: 14,
    paddingVertical: 14,
    gap: 8,
    shadowColor: colors.violet,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.35,
    shadowRadius: 8,
    elevation: 4,
  },
  autoFixButtonDone: {
    backgroundColor: colors.greenLowest,
    shadowColor: colors.greenLowest,
  },
  autoFixButtonText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '800',
  },
});
