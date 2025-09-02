import { API_CONFIG } from '../config/api';

class KeepAliveService {
  private intervalId: NodeJS.Timeout | null = null;
  private readonly PING_INTERVAL = 8 * 60 * 1000; // 8 minutes
  private isActive = false;

  start() {
    if (this.isActive) return;
    
    this.isActive = true;
    console.log('🔄 Starting keep-alive service');
    
    // Initial ping after 30 seconds
    setTimeout(() => this.ping(), 30000);
    
    // Set up regular pings
    this.intervalId = setInterval(() => this.ping(), this.PING_INTERVAL);
  }

  stop() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
      this.intervalId = null;
    }
    this.isActive = false;
    console.log('⏹️ Keep-alive service stopped');
  }

  private async ping() {
    try {
      const response = await fetch(`${API_CONFIG.BASE_URL}/ping`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      
      if (response.ok) {
        console.log('✅ Keep-alive ping successful');
      } else {
        console.warn('⚠️ Keep-alive ping failed:', response.status);
      }
    } catch (error) {
      console.warn('❌ Keep-alive ping error:', error);
    }
  }

  // Ping on user activity
  onUserActivity() {
    if (this.isActive) {
      this.ping();
    }
  }
}

export const keepAliveService = new KeepAliveService();

// Auto-start when user is active
let activityTimeout: NodeJS.Timeout;

const handleUserActivity = () => {
  keepAliveService.onUserActivity();
  
  // Reset activity timer
  clearTimeout(activityTimeout);
  activityTimeout = setTimeout(() => {
    // Stop keep-alive after 30 minutes of inactivity
    keepAliveService.stop();
  }, 30 * 60 * 1000);
};

// Listen for user activity
if (typeof window !== 'undefined') {
  ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'].forEach(event => {
    document.addEventListener(event, handleUserActivity, { passive: true });
  });
}