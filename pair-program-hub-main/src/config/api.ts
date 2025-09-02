// API Configuration
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8086';

export const API_CONFIG = {
  BASE_URL, // API Gateway
  AUTH: {
    // All requests go through API Gateway
    SIGNUP: `${BASE_URL}/auth/signup`,
    VERIFY_OTP: `${BASE_URL}/auth/verify-otp`,
    LOGIN: `${BASE_URL}/auth/login`,
    REFRESH_TOKEN: `${BASE_URL}/auth/refreshAccessToken`
  },
  TOKEN: {
    // These go through API Gateway (need authentication)
    USER_INFO: `${BASE_URL}/token/user-info`,
    VALIDATE: `${BASE_URL}/token/validate`,
    REFRESH: `${BASE_URL}/token/refresh`
  },
  PROFILE: {
    // These go through API Gateway (need authentication)
    GET_PROFILE: `${BASE_URL}/auth/profiles`,
    UPDATE_PROFILE: `${BASE_URL}/auth/profiles`,
    UPLOAD_AVATAR: `${BASE_URL}/auth/profiles`
  },

  PROBLEM: {
    GET_TOPICS: `${BASE_URL}/problem/topicList`,
    GET_PROBLEMS_BY_TOPIC: `${BASE_URL}/problem/topic`,
    GET_PROBLEM_DETAILS: `${BASE_URL}/problem`
  },
  CONTEST: {
    CREATE: `${BASE_URL}/collab/api/contests/create`,
    JOIN: `${BASE_URL}/collab/api/contests/{contestId}/join`,
    GET_DETAILS: `${BASE_URL}/collab/api/contests/{contestId}/details`,
    RUN_CODE: `${BASE_URL}/collab/api/contests/code/run`,
    SUBMIT_CODE: `${BASE_URL}/collab/api/contests/{contestCode}/code/submit`,
    // Dashboard APIs - Enhanced for better user experience
    GET_RECENT_ROOMS: `${BASE_URL}/collab/api/contests/recent-rooms`, // Top 5 recent contest rooms
    GET_ROOM_DASHBOARD_DATA: `${BASE_URL}/collab/api/contests/room/{contestCode}/dashboard-data`, // Detailed room data
    GET_USER_SUBMISSIONS: `${BASE_URL}/collab/api/contests/{contestCode}/submissions` // User's submissions for a contest
  }
};

// LocalStorage keys
export const STORAGE_KEYS = {
  USER_ID: 'userId',
  ACCESS_TOKEN: 'accessToken',
  REFRESH_TOKEN: 'refreshToken',
  USER_PROFILE: 'userProfile',
  USER_INFO: 'userInfo'
};
