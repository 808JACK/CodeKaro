# Collaborative Coding Platform

A real-time collaborative coding platform for teams and contests, similar to LeetCode but with real-time collaboration features.

## Project Structure

### 📁 `/src/pages`
**Main application pages and routing**
- `Index.tsx` - Dashboard with room creation and contest management
- `Room.tsx` - Main collaborative coding room with editor, chat, and problem solving
- `CreateRoom.tsx` - Form to create new collaborative coding rooms
- `CreateContest.tsx` - Form to create timed coding contests with leaderboards
- `SelectProblems.tsx` - Interface for selecting coding problems for rooms/contests
- `NotFound.tsx` - 404 error page

### 📁 `/src/components/room`
**Room-specific components for collaborative coding**
- `MonacoEditor.tsx` - Real-time collaborative code editor with Operational Transform (OT)
- `RoomSidebar.tsx` - Sidebar with participants, chat, and leaderboard
- `TestCasePanel.tsx` - Interactive test case management with separate input parameters
- `ProblemPanel.tsx` - Problem description and navigation interface

### 📁 `/src/components/ui`
**Reusable UI components (shadcn/ui based)**
- Design system components like buttons, cards, dialogs, forms, etc.
- All styled with Tailwind CSS using semantic tokens from the design system

### 📁 `/src/lib`
**Core utilities and services**
- `utils.ts` - General utility functions and class name merging
- `ot-client.ts` - Operational Transform client for real-time collaborative editing
- `websocket.ts` - WebSocket client for real-time communication (currently mock/dummy)

### 📁 `/src/hooks`
**Custom React hooks**
- `use-toast.ts` - Toast notification management
- `use-mobile.tsx` - Mobile device detection

## Key Features

### 🚀 **Collaborative Rooms**
- Real-time code editing with multiple users
- Live cursor positions and selections
- Persistent room state with unique room codes
- Chat system with text messaging
- Problem selection and navigation

### 🏆 **Contest Mode**
- Timed coding contests with individual timers
- Solo coding (no collaboration)
- Live leaderboard updates
- Automatic scoring based on test case results
- Scheduled contest start times (minimum 5 minutes ahead)

### 💻 **Code Editor**
- Monaco Editor with syntax highlighting
- Multiple language support (Python, JavaScript, Java)
- Auto-loading of problem templates
- Real-time synchronization using Operational Transform

### 🧪 **Test Cases**
- Editable test cases with separate input parameter boxes
- Live test execution and result display
- Support for multiple input parameters per problem
- Visual status indicators (passed/failed/running)

## Backend Integration

Currently using mock/dummy APIs and WebSocket connections. The platform is designed to integrate with:
- **PostgreSQL** - User data, room metadata, contest information
- **Cassandra** - Operational Transform logs for real-time editing
- **MongoDB** - Chat messages, room state, code snapshots
- **WebSocket Server** - Real-time communication (see `websocket.ts`)

## Development Status

✅ **Completed:**
- Room creation and joining
- Real-time collaborative editing (frontend)
- Contest management with timers
- Test case management
- Problem selection interface
- Responsive design with dark/light themes

🚧 **In Progress:**
- WebSocket backend integration
- Actual problem API integration
- User authentication
- Leaderboard calculations

## Getting Started

1. Install dependencies: `npm install`
2. Start development server: `npm run dev`
3. Open browser to `http://localhost:5173`

For production deployment, build with `npm run build` and deploy the `dist/` folder.
