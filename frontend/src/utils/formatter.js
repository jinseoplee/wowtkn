/**
 * 주어진 타임스탬프를 현재 시간과 비교하여 'X분/시간/일 전' 형식의 문자열로 포맷한다.
 *
 * @param {number} timestamp - 비교할 UTC 밀리초 단위의 타임스탬프
 * @returns {string} - 포맷된 시간 차이 문자열 (예: 'Just now', '10 minutes ago', '2 hours ago', '3 days ago')
 */
export const getUpdatedAgo = (timestamp) => {
  const diffMs = Date.now() - timestamp;

  const minutes = Math.floor(diffMs / (1000 * 60));
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);

  if (minutes < 1) {
    return "Just now";
  } else if (minutes < 60) {
    return `${minutes} minute${minutes === 1 ? "" : "s"} ago`;
  } else if (hours < 24) {
    return `${hours} hour${hours === 1 ? "" : "s"} ago`;
  } else {
    return `${days} day${days === 1 ? "" : "s"} ago`;
  }
};

/**
 * 숫자를 천 단위 콤마로 포맷한다.
 * @param {number} number - 포맷할 숫자
 * @returns {string} - 포맷된 숫자 문자열 (예: 1234567 -> "1,234,567")
 */
export const formatNumber = (number) => {
  return number.toLocaleString("en-US");
};

/**
 * 양수일 경우 숫자 앞에 '+' 부호를 붙여 포맷한다.
 * @param {number} number - 포맷할 숫자
 * @returns {string} - 부호와 콤마가 포함된 숫자 문자열 (예: 1234 -> "+1,234", -500 -> "-500")
 */
export const formatSignNumber = (number) => {
  if (number > 0) {
    return `+${number.toLocaleString("en-US")}`;
  }
  return number.toLocaleString("en-US");
};

/**
 * 변화율에 부호(+/-)와 퍼센트(%) 기호를 포함하여 소수점 첫째 자리까지 포맷한다.
 * @param {number} rate - 포맷할 변화율
 * @returns {string} - 부호와 퍼센트 기호가 포함된 변화율 문자열 (예: 0.8 -> "+0.8%")
 */
export const formatChangeRate = (rate) => {
  if (rate > 0) {
    return `+${rate.toFixed(1)}%`;
  }
  return `${rate.toFixed(1)}%`;
};

/**
 * 주어진 timestamp를 지정한 시간대 기준으로 "MM-DD HH:mm" 형식으로 포맷한다.
 *
 * @param {number} timestamp - UNIX timestamp (밀리초 단위)
 * @param {string} timeZone - IANA 표준 시간대 (예: "America/New_York", "Asia/Seoul")
 * @returns {string} - "MM-DD HH:mm" 포맷의 문자열 (예: "07-15 09:00")
 */
export const formatDateTime = (timestamp, timeZone) => {
  const date = new Date(timestamp);
  const options = {
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
    timeZone: timeZone,
  };
  return date
    .toLocaleString("en-US", options)
    .replace(/,/g, "")
    .replace(/\//g, "-");
};
