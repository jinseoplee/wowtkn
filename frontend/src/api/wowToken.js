import api from "./axiosInstance";

export function getWowTokens(region = "US", days = 7) {
  return api.get(`/api/wow-tokens/${region}`, { params: { days } });
}
