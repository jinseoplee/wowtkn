import { formatTimeAgo } from "./time";

export const getCurrentPrice = (wowTokens) => {
  if (!wowTokens.length) {
    return {
      currentPrice: null,
      changeAmount: null,
      changeRate: null,
      timeAgo: null,
    };
  }

  const latest = wowTokens[wowTokens.length - 1];
  const prev = wowTokens[wowTokens.length - 2];

  const changeAmount = prev ? latest.price - prev.price : null;
  const changeRate = prev ? (changeAmount / prev.price) * 100 : null;

  return {
    currentPrice: latest.price,
    changeAmount,
    changeRate,
    timeAgo: formatTimeAgo(latest.timestamp),
  };
};
