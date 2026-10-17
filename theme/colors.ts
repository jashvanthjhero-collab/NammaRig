export const colors = {
  canvas: '#ECEDF5',
  card: '#FFFFFF',
  violet: '#6C4BF4',
  violetSoft: '#EEECFC',
  violetGlow: 'rgba(108, 75, 244, 0.35)',
  ink: '#090D16',
  muted: '#8F92AB',
  border: '#E4E3F0',
  greenLowest: '#059669',
  greenBg: '#ECFDF5',
} as const;

export type ColorToken = keyof typeof colors;
