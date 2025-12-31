'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index$2 = require('../../icon/index.js');
var iconsVue = require('@element-plus/icons-vue');
var carousel = require('./carousel.js');
var useCarousel = require('./use-carousel.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var index = require('../../../hooks/use-namespace/index.js');
var index$1 = require('../../../hooks/use-locale/index.js');

const _hoisted_1 = ["aria-label"];
const _hoisted_2 = ["aria-label"];
const _hoisted_3 = ["onMouseenter", "onClick"];
const _hoisted_4 = ["aria-label"];
const _hoisted_5 = { key: 0 };
const _hoisted_6 = {
  key: 2,
  xmlns: "http://www.w3.org/2000/svg",
  version: "1.1",
  style: { "display": "none" }
};
const COMPONENT_NAME = "ElCarousel";
const _sfc_main = vue.defineComponent({
  ...{
    name: COMPONENT_NAME
  },
  __name: "carousel",
  props: carousel.carouselProps,
  emits: carousel.carouselEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const {
      root,
      activeIndex,
      exposeActiveIndex,
      arrowDisplay,
      hasLabel,
      hover,
      isCardType,
      items,
      isVertical,
      containerStyle,
      handleButtonEnter,
      handleButtonLeave,
      handleIndicatorClick,
      handleMouseEnter,
      handleMouseLeave,
      setActiveItem,
      prev,
      next,
      PlaceholderItem,
      isTwoLengthShow,
      ItemsSorter,
      throttledArrowClick,
      throttledIndicatorHover
    } = useCarousel.useCarousel(props, emit, COMPONENT_NAME);
    const ns = index.useNamespace("carousel");
    const { t } = index$1.useLocale();
    const carouselClasses = vue.computed(() => {
      const classes = [ns.b(), ns.m(props.direction)];
      if (vue.unref(isCardType)) {
        classes.push(ns.m("card"));
      }
      return classes;
    });
    const indicatorsClasses = vue.computed(() => {
      const classes = [ns.e("indicators"), ns.em("indicators", props.direction)];
      if (vue.unref(hasLabel)) {
        classes.push(ns.em("indicators", "labels"));
      }
      if (props.indicatorPosition === "outside") {
        classes.push(ns.em("indicators", "outside"));
      }
      if (vue.unref(isVertical)) {
        classes.push(ns.em("indicators", "right"));
      }
      return classes;
    });
    function handleTransitionStart(e) {
      if (!props.motionBlur)
        return;
      const kls = vue.unref(isVertical) ? `${ns.namespace.value}-transitioning-vertical` : `${ns.namespace.value}-transitioning`;
      e.currentTarget.classList.add(kls);
    }
    function handleTransitionEnd(e) {
      if (!props.motionBlur)
        return;
      const kls = vue.unref(isVertical) ? `${ns.namespace.value}-transitioning-vertical` : `${ns.namespace.value}-transitioning`;
      e.currentTarget.classList.remove(kls);
    }
    __expose({
      activeIndex: exposeActiveIndex,
      setActiveItem,
      prev,
      next
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock(
        "div",
        {
          ref_key: "root",
          ref: root,
          class: vue.normalizeClass(carouselClasses.value),
          onMouseenter: _cache[6] || (_cache[6] = vue.withModifiers(
            (...args) => vue.unref(handleMouseEnter) && vue.unref(handleMouseEnter)(...args),
            ["stop"]
          )),
          onMouseleave: _cache[7] || (_cache[7] = vue.withModifiers(
            (...args) => vue.unref(handleMouseLeave) && vue.unref(handleMouseLeave)(...args),
            ["stop"]
          ))
        },
        [
          vue.unref(arrowDisplay) ? (vue.openBlock(), vue.createBlock(vue.Transition, {
            key: 0,
            name: "carousel-arrow-left",
            persisted: ""
          }, {
            default: vue.withCtx(() => [
              vue.withDirectives(vue.createElementVNode("button", {
                type: "button",
                class: vue.normalizeClass([vue.unref(ns).e("arrow"), vue.unref(ns).em("arrow", "left")]),
                "aria-label": vue.unref(t)("el.carousel.leftArrow"),
                onMouseenter: _cache[0] || (_cache[0] = ($event) => vue.unref(handleButtonEnter)("left")),
                onMouseleave: _cache[1] || (_cache[1] = (...args) => vue.unref(handleButtonLeave) && vue.unref(handleButtonLeave)(...args)),
                onClick: _cache[2] || (_cache[2] = vue.withModifiers(($event) => vue.unref(throttledArrowClick)(vue.unref(activeIndex) - 1), ["stop"]))
              }, [
                vue.createVNode(vue.unref(index$2.ElIcon), null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(vue.unref(iconsVue.ArrowLeft))
                  ]),
                  _: 1
                })
              ], 42, _hoisted_1), [
                [vue.vShow, (_ctx.arrow === "always" || vue.unref(hover)) && (_ctx.loop || vue.unref(activeIndex) > 0)]
              ])
            ]),
            _: 1
          })) : vue.createCommentVNode("v-if", true),
          vue.unref(arrowDisplay) ? (vue.openBlock(), vue.createBlock(vue.Transition, {
            key: 1,
            name: "carousel-arrow-right",
            persisted: ""
          }, {
            default: vue.withCtx(() => [
              vue.withDirectives(vue.createElementVNode("button", {
                type: "button",
                class: vue.normalizeClass([vue.unref(ns).e("arrow"), vue.unref(ns).em("arrow", "right")]),
                "aria-label": vue.unref(t)("el.carousel.rightArrow"),
                onMouseenter: _cache[3] || (_cache[3] = ($event) => vue.unref(handleButtonEnter)("right")),
                onMouseleave: _cache[4] || (_cache[4] = (...args) => vue.unref(handleButtonLeave) && vue.unref(handleButtonLeave)(...args)),
                onClick: _cache[5] || (_cache[5] = vue.withModifiers(($event) => vue.unref(throttledArrowClick)(vue.unref(activeIndex) + 1), ["stop"]))
              }, [
                vue.createVNode(vue.unref(index$2.ElIcon), null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(vue.unref(iconsVue.ArrowRight))
                  ]),
                  _: 1
                })
              ], 42, _hoisted_2), [
                [
                  vue.vShow,
                  (_ctx.arrow === "always" || vue.unref(hover)) && (_ctx.loop || vue.unref(activeIndex) < vue.unref(items).length - 1)
                ]
              ])
            ]),
            _: 1
          })) : vue.createCommentVNode("v-if", true),
          vue.createElementVNode(
            "div",
            {
              class: vue.normalizeClass(vue.unref(ns).e("container")),
              style: vue.normalizeStyle(vue.unref(containerStyle)),
              onTransitionstart: handleTransitionStart,
              onTransitionend: handleTransitionEnd
            },
            [
              vue.createVNode(vue.unref(PlaceholderItem)),
              vue.renderSlot(_ctx.$slots, "default")
            ],
            38
          ),
          vue.createVNode(vue.unref(ItemsSorter), null, {
            default: vue.withCtx(() => [
              _ctx.indicatorPosition !== "none" ? (vue.openBlock(), vue.createElementBlock(
                "ul",
                {
                  key: 0,
                  class: vue.normalizeClass(indicatorsClasses.value)
                },
                [
                  (vue.openBlock(true), vue.createElementBlock(
                    vue.Fragment,
                    null,
                    vue.renderList(vue.unref(items), (item, index) => {
                      return vue.withDirectives((vue.openBlock(), vue.createElementBlock("li", {
                        key: index,
                        class: vue.normalizeClass([
                          vue.unref(ns).e("indicator"),
                          vue.unref(ns).em("indicator", _ctx.direction),
                          vue.unref(ns).is("active", index === vue.unref(activeIndex))
                        ]),
                        onMouseenter: ($event) => vue.unref(throttledIndicatorHover)(index),
                        onClick: vue.withModifiers(($event) => vue.unref(handleIndicatorClick)(index), ["stop"])
                      }, [
                        vue.createElementVNode("button", {
                          class: vue.normalizeClass(vue.unref(ns).e("button")),
                          "aria-label": vue.unref(t)("el.carousel.indicator", { index: index + 1 })
                        }, [
                          vue.unref(hasLabel) ? (vue.openBlock(), vue.createElementBlock(
                            "span",
                            _hoisted_5,
                            vue.toDisplayString(item.props.label),
                            1
                          )) : vue.createCommentVNode("v-if", true)
                        ], 10, _hoisted_4)
                      ], 42, _hoisted_3)), [
                        [vue.vShow, vue.unref(isTwoLengthShow)(index)]
                      ]);
                    }),
                    128
                  ))
                ],
                2
              )) : vue.createCommentVNode("v-if", true)
            ]),
            _: 1
          }),
          _ctx.motionBlur ? (vue.openBlock(), vue.createElementBlock("svg", _hoisted_6, [..._cache[8] || (_cache[8] = [
            vue.createElementVNode(
              "defs",
              null,
              [
                vue.createElementVNode("filter", { id: "elCarouselHorizontal" }, [
                  vue.createElementVNode("feGaussianBlur", {
                    in: "SourceGraphic",
                    stdDeviation: "12,0"
                  })
                ]),
                vue.createElementVNode("filter", { id: "elCarouselVertical" }, [
                  vue.createElementVNode("feGaussianBlur", {
                    in: "SourceGraphic",
                    stdDeviation: "0,10"
                  })
                ])
              ],
              -1
            )
          ])])) : vue.createCommentVNode("v-if", true)
        ],
        34
      );
    };
  }
});
var Carousel = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/carousel/src/carousel.vue"]]);

exports["default"] = Carousel;
//# sourceMappingURL=carousel2.js.map
