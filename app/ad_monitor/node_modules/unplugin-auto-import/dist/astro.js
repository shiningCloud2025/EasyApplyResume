import {
  unplugin_default
} from "./chunk-GR6VF5HE.js";
import "./chunk-S4VTQKPB.js";

// src/astro.ts
function astro_default(options) {
  return {
    name: "unplugin-auto-import",
    hooks: {
      "astro:config:setup": async (astro) => {
        var _a;
        (_a = astro.config.vite).plugins || (_a.plugins = []);
        astro.config.vite.plugins.push(unplugin_default.vite(options));
      }
    }
  };
}
export {
  astro_default as default
};
