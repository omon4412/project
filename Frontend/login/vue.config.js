const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    devServer: {
        port: 5000
    },
    transpileDependencies: true
})
