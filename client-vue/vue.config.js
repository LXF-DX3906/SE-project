module.exports = {
    devServer: {
        open: true,
        host: '0.0.0.0',
        port: 8000,
        https: false,
        hotOnly: false,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    },
    productionSourceMap:false,
};
