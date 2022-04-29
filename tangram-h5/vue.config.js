module.exports = {
    configureWebpack: config => {
        config.module.rules.push({
            test: /\.md$/,
            use: [
                {
                    loader: 'html-loader',
                },
                {
                    loader: 'markdown-loader',
                    options: {},
                },
            ],
        })
    },
    css: {
        loaderOptions: {
            less: {
                modifyVars: {
                    'searchform-bgcolor': '#fbfbfb',
                    'primary-color': '#16817a',
                },
                javascriptEnabled: true,
            },
        },
    },
    devServer: {
        hot: true,
        host: 'web.xxx.com', //配置此host 127.0.0.1 web.xxx.com
        port: 8080,
        open: true,
        https: true,
        proxy: {
            // '/sso': {
            //     target: 'https://dev-sso.amh-group.com/',
            //     ws: false,
            //     changOrigin: true,
            //     secure: true,
            //     pathRewrite: {
            //         '^/sso/': '',
            //     },
            // },

            '/tangram': {
                //本地
                target: 'http://web.xxx.com:9090/tangram/',
                //线上
                //target: 'http://tangram.xxx.com/tangram/',
                ws: false,
                changOrigin: true,
                secure: true,
                pathRewrite: {
                    '^/tangram/': '',
                },
            },
        },
    },
}
