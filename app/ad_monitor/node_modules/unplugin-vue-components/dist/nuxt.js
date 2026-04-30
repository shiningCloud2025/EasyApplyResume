import {
  unplugin_default
} from "./chunk-LAHXDHMT.js";
import "./chunk-WRE7G5OD.js";
import "./chunk-5JVO2UWC.js";
import "./chunk-6F4PWJZI.js";

// src/nuxt.ts
import { addVitePlugin, addWebpackPlugin, defineNuxtModule } from "@nuxt/kit";
var nuxt_default = defineNuxtModule({
  setup(options) {
    addWebpackPlugin(unplugin_default.webpack(options));
    addVitePlugin(unplugin_default.vite(options));
  }
});
export {
  nuxt_default as default
};
