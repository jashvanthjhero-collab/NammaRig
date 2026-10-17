import React from 'react';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { NavigationContainer, DefaultTheme } from '@react-navigation/native';
import { RootNavigator } from './navigation/RootNavigator';
import { colors } from './theme/colors';

const NammaRigNavTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    background: colors.canvas,
    card: colors.card,
    text: colors.ink,
    border: colors.border,
    primary: colors.violet,
  },
};

export default function App() {
  return (
    <SafeAreaProvider>
      <NavigationContainer theme={NammaRigNavTheme}>
        <StatusBar style="dark" backgroundColor={colors.canvas} />
        <RootNavigator />
      </NavigationContainer>
    </SafeAreaProvider>
  );
}
