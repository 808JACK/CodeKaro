const express = require('express');
const cron = require('node-cron');
const axios = require('axios');

const app = express();
const PORT = process.env.PORT || 3000;

// Service URLs - update these with your actual Render URLs
const services = [
    { name: 'API Gateway', url: process.env.API_GATEWAY_URL || 'https://your-api-gateway.onrender.com' },
    { name: 'Auth Service', url: process.env.AUTH_SERVICE_URL || 'https://your-auth-service.onrender.com' },
    { name: 'Problem Service', url: process.env.PROBLEM_SERVICE_URL || 'https://your-problem-service.onrender.com' },
    { name: 'Collab Service', url: process.env.COLLAB_SERVICE_URL || 'https://your-collab-service.onrender.com' },
    { name: 'Eureka Service', url: process.env.EUREKA_SERVICE_URL || 'https://your-eureka-service.onrender.com' }
];

// Ping function
async function pingService(service) {
    try {
        const response = await axios.get(`${service.url}/ping`, {
            timeout: 10000,
            headers: {
                'User-Agent': 'CodeKaro-KeepAlive/1.0'
            }
        });
        console.log(`✅ ${service.name}: ${response.status} - ${response.data}`);
        return true;
    } catch (error) {
        console.log(`❌ ${service.name}: ${error.message}`);
        return false;
    }
}

// Ping all services
async function pingAllServices() {
    console.log(`\n🔄 Pinging services at ${new Date().toISOString()}`);
    const results = await Promise.allSettled(
        services.map(service => pingService(service))
    );
    
    const successful = results.filter(r => r.status === 'fulfilled' && r.value).length;
    console.log(`📊 ${successful}/${services.length} services responded successfully\n`);
}

// Schedule pings every 5 minutes (aggressive keep-alive)
cron.schedule('*/5 * * * *', () => {
    pingAllServices();
});

// Health endpoint for this service
app.get('/health', (req, res) => {
    res.json({ 
        status: 'healthy', 
        timestamp: new Date().toISOString(),
        services: services.length 
    });
});

// Manual ping endpoint
app.get('/ping-now', async (req, res) => {
    await pingAllServices();
    res.json({ message: 'Ping completed', timestamp: new Date().toISOString() });
});

// Status endpoint
app.get('/status', (req, res) => {
    res.json({
        message: 'CodeKaro Keep-Alive Service',
        services: services.map(s => ({ name: s.name, url: s.url })),
        nextPing: 'Every 10 minutes',
        uptime: process.uptime()
    });
});

app.listen(PORT, () => {
    console.log(`🚀 Keep-Alive service running on port ${PORT}`);
    console.log(`📋 Monitoring ${services.length} services`);
    console.log(`⏰ Pinging every 5 minutes (aggressive keep-alive)`);
    
    // Initial ping
    setTimeout(() => {
        pingAllServices();
    }, 5000);
});