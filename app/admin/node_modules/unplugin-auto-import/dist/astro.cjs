"use strict";Object.defineProperty(exports, "__esModule", {value: true});

var _chunkCXITMYZHcjs = require('./chunk-CXITMYZH.cjs');
require('./chunk-53W4H52Q.cjs');

// src/astro.ts
function astro_default(options) {
  return {
    name: "unplugin-auto-import",
    hooks: {
      "astro:config:setup": async (astro) => {
        var _a;
        (_a = astro.config.vite).plugins || (_a.plugins = []);
        astro.config.vite.plugins.push(_chunkCXITMYZHcjs.unplugin_default.vite(options));
      }
    }
  };
}


exports.default = astro_default;
