// WebSocket client for real-time collaboration
// Currently using dummy/mock implementation - uncomment when backend is ready

export interface WebSocketMessage {
  type: 'operation' | 'selection' | 'chat' | 'join' | 'leave' | 'test_result';
  data: any;
  userId?: string;
  roomId?: string;
  timestamp?: number;
}

export class RoomWebSocket {
  private ws: WebSocket | null = null;
  private roomId: string;
  private userId: string;
  private handlers: Map<string, Function[]> = new Map();

  constructor(roomId: string, userId: string) {
    this.roomId = roomId;
    this.userId = userId;
  }

  // Dummy connection - replace with actual WebSocket URL when backend is ready
  connect() {
    console.log(`[WebSocket] Connecting to room ${this.roomId} as user ${this.userId}`);
    
    // TODO: Uncomment when WebSocket server is ready
    // this.ws = new WebSocket(`ws://localhost:8080/room/${this.roomId}`);
    
    // this.ws.onopen = () => {
    //   console.log('[WebSocket] Connected');
    //   this.send({ type: 'join', data: { userId: this.userId } });
    // };

    // this.ws.onmessage = (event) => {
    //   const message: WebSocketMessage = JSON.parse(event.data);
    //   this.handleMessage(message);
    // };

    // this.ws.onclose = () => {
    //   console.log('[WebSocket] Disconnected');
    // };

    // this.ws.onerror = (error) => {
    //   console.error('[WebSocket] Error:', error);
    // };

    // Mock simulation for development
    this.simulateDummyConnection();
  }

  private simulateDummyConnection() {
    // Simulate connection events for development
    setTimeout(() => {
      this.emit('connected', { roomId: this.roomId, userId: this.userId });
    }, 100);

    // Simulate periodic user activity
    setInterval(() => {
      this.emit('user_activity', {
        users: [
          { id: this.userId, name: 'You', isOnline: true },
          { id: 'user2', name: 'Alice', isOnline: true },
          { id: 'user3', name: 'Bob', isOnline: Math.random() > 0.5 }
        ]
      });
    }, 5000);
  }

  send(message: WebSocketMessage) {
    message.userId = this.userId;
    message.roomId = this.roomId;
    message.timestamp = Date.now();

    console.log('[WebSocket] Sending:', message);

    // TODO: Uncomment when WebSocket server is ready
    // if (this.ws && this.ws.readyState === WebSocket.OPEN) {
    //   this.ws.send(JSON.stringify(message));
    // }

    // Mock response for development
    this.simulateResponse(message);
  }

  private simulateResponse(message: WebSocketMessage) {
    // Simulate server responses for development
    setTimeout(() => {
      switch (message.type) {
        case 'operation':
          // Echo operation to simulate real-time sync
          this.emit('operation_received', message.data);
          break;
        case 'chat':
          // Echo chat message
          this.emit('chat_message', {
            id: Date.now().toString(),
            userId: this.userId,
            userName: 'You',
            message: message.data.message,
            timestamp: Date.now()
          });
          break;
        case 'test_result':
          // Simulate test execution
          this.emit('test_completed', {
            testCaseId: message.data.testCaseId,
            status: Math.random() > 0.5 ? 'passed' : 'failed',
            actualOutput: message.data.expectedOutput,
            runtime: Math.floor(Math.random() * 1000)
          });
          break;
      }
    }, 500);
  }

  private handleMessage(message: WebSocketMessage) {
    this.emit(message.type, message.data);
  }

  on(event: string, handler: Function) {
    if (!this.handlers.has(event)) {
      this.handlers.set(event, []);
    }
    this.handlers.get(event)!.push(handler);
  }

  off(event: string, handler: Function) {
    const handlers = this.handlers.get(event);
    if (handlers) {
      const index = handlers.indexOf(handler);
      if (index > -1) {
        handlers.splice(index, 1);
      }
    }
  }

  private emit(event: string, data: any) {
    const handlers = this.handlers.get(event);
    if (handlers) {
      handlers.forEach(handler => handler(data));
    }
  }

  disconnect() {
    console.log('[WebSocket] Disconnecting');
    
    // TODO: Uncomment when WebSocket server is ready
    // if (this.ws) {
    //   this.ws.close();
    //   this.ws = null;
    // }
    
    this.handlers.clear();
  }

  // Helper methods for common operations
  sendOperation(operation: any) {
    this.send({ type: 'operation', data: operation });
  }

  sendSelection(selection: any) {
    this.send({ type: 'selection', data: selection });
  }

  sendChatMessage(message: string) {
    this.send({ type: 'chat', data: { message } });
  }

  runTestCase(testCase: any) {
    this.send({ type: 'test_result', data: testCase });
  }
}

export default RoomWebSocket;