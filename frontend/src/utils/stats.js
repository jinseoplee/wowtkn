const MS_PER_DAY = 24 * 60 * 60 * 1000;

const PERIODS = {
  "1d": 1 * MS_PER_DAY,
  "1w": 7 * MS_PER_DAY,
  "1m": 30 * MS_PER_DAY,
};

export const getPriceStatsByPeriod = (wowTokens) => {
  const now = Date.now();

  const initial = Object.fromEntries(
    Object.keys(PERIODS).map((period) => [
      period,
      { min: Infinity, max: -Infinity },
    ])
  );

  if (!wowTokens.length) {
    return Object.fromEntries(
      Object.keys(PERIODS).map((period) => [period, { min: null, max: null }])
    );
  }

  const stats = wowTokens.reduce((acc, wowToken) => {
    for (const [period, duration] of Object.entries(PERIODS)) {
      if (wowToken.timestamp >= now - duration) {
        acc[period].min = Math.min(acc[period].min, wowToken.price);
        acc[period].max = Math.max(acc[period].max, wowToken.price);
      }
    }
    return acc;
  }, initial);

  for (const period of Object.keys(stats)) {
    if (stats[period].min === Infinity) stats[period].min = null;
    if (stats[period].max === -Infinity) stats[period].max = null;
  }

  return stats;
};
