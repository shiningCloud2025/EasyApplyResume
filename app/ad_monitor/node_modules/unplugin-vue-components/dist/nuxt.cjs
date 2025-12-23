"use strict";Object.defineProperty(exports, "__esModule", {value: true});

var _chunkGNWVEWZXcjs = require('./chunk-GNWVEWZX.cjs');
require('./chunk-UX65OJTH.cjs');
require('./chunk-WJIU4Q36.cjs');
require('./chunk-ZBPRDZS4.cjs');

// src/nuxt.ts
var _kit = require('@nuxt/kit');
var nuxt_default = _kit.defineNuxtModule.call(void 0, {
  setup(options) {
    _kit.addWebpackPlugin.call(void 0, _chunkGNWVEWZXcjs.unplugin_default.webpack(options));
    _kit.addVitePlugin.call(void 0, _chunkGNWVEWZXcjs.unplugin_default.vite(options));
  }
});


exports.default = nuxt_default;
