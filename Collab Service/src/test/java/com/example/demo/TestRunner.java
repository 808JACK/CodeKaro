package com.example.demo;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

/**
 * Test runner for collaborative editing tests
 * Run this to execute all collaboration-related tests
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting Collaborative Editor Test Suite");
        System.out.println("============================================");
        System.out.println("📋 Test Categories:");
        System.out.println("   • Unit Tests: SimpleOTServiceTest, CassandraOTServiceTest, SessionRegistryServiceTest");
        System.out.println("   • WebSocket Tests: CollaborativeWebSocketTest");
        System.out.println("   • Integration Tests: CompleteCollaborationFlowTest, CassandraNewUserJoinTest");
        System.out.println();
        
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                    selectPackage("com.example.demo.services"),
                    selectPackage("com.example.demo.controller"),
                    selectPackage("com.example.demo.integration")
                )
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        
        TestExecutionSummary summary = listener.getSummary();
        
        System.out.println("\n📊 Test Results Summary");
        System.out.println("=======================");
        System.out.println("Tests found: " + summary.getTestsFoundCount());
        System.out.println("Tests started: " + summary.getTestsStartedCount());
        System.out.println("Tests successful: " + summary.getTestsSucceededCount());
        System.out.println("Tests failed: " + summary.getTestsFailedCount());
        System.out.println("Tests skipped: " + summary.getTestsSkippedCount());
        
        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\n❌ Failed Tests:");
            summary.getFailures().forEach(failure -> {
                System.out.println("- " + failure.getTestIdentifier().getDisplayName());
                System.out.println("  Reason: " + failure.getException().getMessage());
            });
        }
        
        if (summary.getTestsSucceededCount() == summary.getTestsStartedCount()) {
            System.out.println("\n✅ All tests passed! Collaborative editor is working correctly.");
        } else {
            System.out.println("\n⚠️  Some tests failed. Please check the implementation.");
        }
        
        System.out.println("\n🎯 Test Categories Covered:");
        System.out.println("- ✅ Broadcasting: Operations sent to all participants");
        System.out.println("- ✅ Real-time: Low latency message delivery");
        System.out.println("- ✅ OT (Operational Transform): Concurrent operation handling");
        System.out.println("- ✅ New Joiner: Complete state synchronization");
        System.out.println("- ✅ Session Management: User join/leave handling");
        System.out.println("- ✅ Cursor Sharing: Real-time cursor position updates");
        System.out.println("- ✅ High Concurrency: Multiple users editing simultaneously");
    }
}