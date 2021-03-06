module.exports = {
    devServer: {
        open: true,
        host: '0.0.0.0',
        port: 8080,
        https: false,
        hotOnly: false,
        proxy: {
            '/api': {
                target: 'http://localhost:8021',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': 'http://localhost:8021'
                    // '^/api': 'http://localhost:8021/api'
                }
            }
        }
    },
    productionSourceMap:false,
};
