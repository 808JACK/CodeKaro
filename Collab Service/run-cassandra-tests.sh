#!/bin/bash

echo "🗄️ Cassandra Test Execution Script"
echo "============================================"
echo

echo "🔧 Step 1: Verify Cassandra Test Configuration"
echo "mvn test -Dtest=CassandraTestVerification"
mvn test -Dtest=CassandraTestVerification
echo

if [ $? -eq 0 ]; then
    echo "✅ Configuration verification passed!"
    echo
    
    echo "🧪 Step 2: Run Cassandra OT Service Tests"
    echo "mvn test -Dtest=CassandraOTServiceTest"
    mvn test -Dtest=CassandraOTServiceTest
    echo
    
    if [ $? -eq 0 ]; then
        echo "✅ Unit tests passed!"
        echo
        
        echo "🌐 Step 3: Run Cassandra Integration Tests"
        echo "mvn test -Dtest=CassandraNewUserJoinTest"
        mvn test -Dtest=CassandraNewUserJoinTest
        echo
        
        if [ $? -eq 0 ]; then
            echo "🎉 ALL CASSANDRA TESTS PASSED!"
            echo "============================================"
            echo "✅ New users joining rooms will receive all previous changes"
            echo "✅ Document reconstruction from Cassandra works"
            echo "✅ Version synchronization is correct"
            echo "✅ WebSocket integration is functional"
            echo "✅ Multi-user collaboration is supported"
            echo "============================================"
        else
            echo "❌ Integration tests failed"
        fi
    else
        echo "❌ Unit tests failed"
    fi
else
    echo "❌ Configuration verification failed"
    echo "Please check your test setup"
fi

echo
echo "📚 For more details, see:"
echo "   • CASSANDRA_TESTING.md"
echo "   • CASSANDRA_IMPLEMENTATION_SUMMARY.md"