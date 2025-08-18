// API Configuration
export const API_CONFIG = {
  BASE_URL: 'http://localhost:8086',
  AUTH: {
    SIGNUP: 'http://localhost:8086/auth/signup',
    VERIFY_OTP: 'http://localhost:8086/auth/verify-otp',
    LOGIN: 'http://localhost:8086/auth/login',
    REFRESH_TOKEN: 'http://localhost:8086/auth/refreshAccessToken'
  },
  TOKEN: {
    USER_INFO: 'http://localhost:8086/token/user-info',
    VALIDATE: 'http://localhost:8086/token/validate',
    REFRESH: 'http://localhost:8086/token/refresh'
  },
  PROFILE: {
    GET_PROFILE: 'http://localhost:8086/auth/profiles',
    UPDATE_PROFILE: 'http://localhost:8086/auth/profiles',
    UPLOAD_AVATAR: 'http://localhost:8086/auth/profiles'
  },
  PROBLEM: {
    GET_TOPICS: 'http://localhost:8086/problem/topicList',
    GET_PROBLEMS_BY_TOPIC: 'http://localhost:8086/problem/topic',
    GET_PROBLEM_DETAILS: 'http://localhost:8086/problem'
  },
  CONTEST: {
    CREATE: 'http://localhost:8092/api/contests/create',
    JOIN: 'http://localhost:8092/api/contests/{contestId}/join',
    GET_DETAILS: 'http://localhost:8092/api/contests/{contestId}/details',
    RUN_CODE: 'http://localhost:8092/api/contests/code/run',
    SUBMIT_CODE: 'http://localhost:8092/api/contests/{contestCode}/code/submit',
    // Dashboard APIs - Enhanced for better user experience
    GET_RECENT_ROOMS: 'http://localhost:8092/api/contests/recent-rooms', // Top 5 recent contest rooms
    GET_ROOM_DASHBOARD_DATA: 'http://localhost:8092/api/contests/room/{contestCode}/dashboard-data', // Detailed room data
    GET_USER_SUBMISSIONS: 'http://localhost:8092/api/contests/{contestCode}/submissions' // User's submissions for a contest
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
