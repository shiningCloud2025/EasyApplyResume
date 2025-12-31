"use strict";Object.defineProperty(exports, "__esModule", {value: true});

var _chunkCXITMYZHcjs = require('./chunk-CXITMYZH.cjs');
require('./chunk-53W4H52Q.cjs');

// src/nuxt.ts
var _kit = require('@nuxt/kit');
var nuxt_default = _kit.defineNuxtModule.call(void 0, {
  setup(options) {
    options.exclude = options.exclude || [/[\\/]node_modules[\\/]/, /[\\/]\.git[\\/]/, /[\\/]\.nuxt[\\/]/];
    _kit.addWebpackPlugin.call(void 0, _chunkCXITMYZHcjs.unplugin_default.webpack(options));
    _kit.addVitePlugin.call(void 0, _chunkCXITMYZHcjs.unplugin_default.vite(options));
  }
});


exports.default = nuxt_default;
