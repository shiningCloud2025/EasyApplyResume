'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var index = require('../../checkbox/index.js');
var index$3 = require('../../icon/index.js');
var iconsVue = require('@element-plus/icons-vue');
var index$2 = require('../../tooltip/index.js');
var index$1 = require('../../scrollbar/index.js');
var pluginVue_exportHelper = require('../../../_virtual/plugin-vue_export-helper.js');
var content = require('../../tooltip/src/content.js');
var index$4 = require('../../../hooks/use-locale/index.js');
var index$5 = require('../../../hooks/use-namespace/index.js');
var types = require('../../../utils/types.js');
var event = require('../../../utils/dom/event.js');
var aria = require('../../../constants/aria.js');

const _sfc_main = vue.defineComponent({
  name: "ElTableFilterPanel",
  components: {
    ElCheckbox: index.ElCheckbox,
    ElCheckboxGroup: index.ElCheckboxGroup,
    ElScrollbar: index$1.ElScrollbar,
    ElTooltip: index$2.ElTooltip,
    ElIcon: index$3.ElIcon,
    ArrowDown: iconsVue.ArrowDown,
    ArrowUp: iconsVue.ArrowUp
  },
  props: {
    placement: {
      type: String,
      default: "bottom-start"
    },
    store: {
      type: Object
    },
    column: {
      type: Object
    },
    upDataColumn: {
      type: Function
    },
    appendTo: content.useTooltipContentProps.appendTo
  },
  setup(props) {
    const instance = vue.getCurrentInstance();
    const { t } = index$4.useLocale();
    const ns = index$5.useNamespace("table-filter");
    const parent = instance == null ? void 0 : instance.parent;
    if (props.column && !parent.filterPanels.value[props.column.id]) {
      parent.filterPanels.value[props.column.id] = instance;
    }
    const tooltipRef = vue.ref(null);
    const rootRef = vue.ref(null);
    const checkedIndex = vue.ref(0);
    const filters = vue.computed(() => {
      return props.column && props.column.filters;
    });
    const filterClassName = vue.computed(() => {
      if (props.column && props.column.filterClassName) {
        return `${ns.b()} ${props.column.filterClassName}`;
      }
      return ns.b();
    });
    const filterValue = vue.computed({
      get: () => {
        var _a;
        return (((_a = props.column) == null ? void 0 : _a.filteredValue) || [])[0];
      },
      set: (value) => {
        if (filteredValue.value) {
          if (!types.isPropAbsent(value)) {
            filteredValue.value.splice(0, 1, value);
          } else {
            filteredValue.value.splice(0, 1);
          }
        }
      }
    });
    const filteredValue = vue.computed({
      get() {
        if (props.column) {
          return props.column.filteredValue || [];
        }
        return [];
      },
      set(value) {
        var _a;
        if (props.column) {
          (_a = props.upDataColumn) == null ? void 0 : _a.call(props, "filteredValue", value);
        }
      }
    });
    const multiple = vue.computed(() => {
      if (props.column) {
        return props.column.filterMultiple;
      }
      return true;
    });
    const isActive = (filter) => {
      return filter.value === filterValue.value;
    };
    const hidden = () => {
      var _a;
      (_a = tooltipRef.value) == null ? void 0 : _a.onClose();
    };
    const handleConfirm = () => {
      confirmFilter(filteredValue.value);
      hidden();
    };
    const handleReset = () => {
      filteredValue.value = [];
      confirmFilter(filteredValue.value);
      hidden();
    };
    const handleSelect = (_filterValue, index) => {
      filterValue.value = _filterValue;
      checkedIndex.value = index;
      if (!types.isPropAbsent(_filterValue)) {
        confirmFilter(filteredValue.value);
      } else {
        confirmFilter([]);
      }
      hidden();
    };
    const confirmFilter = (filteredValue2) => {
      var _a, _b;
      (_a = props.store) == null ? void 0 : _a.commit("filterChange", {
        column: props.column,
        values: filteredValue2
      });
      (_b = props.store) == null ? void 0 : _b.updateAllSelected();
    };
    const handleShowTooltip = () => {
      var _a, _b;
      (_a = rootRef.value) == null ? void 0 : _a.focus();
      !multiple.value && initCheckedIndex();
      if (props.column) {
        (_b = props.upDataColumn) == null ? void 0 : _b.call(props, "filterOpened", true);
      }
    };
    const handleHideTooltip = () => {
      var _a;
      if (props.column) {
        (_a = props.upDataColumn) == null ? void 0 : _a.call(props, "filterOpened", false);
      }
    };
    const initCheckedIndex = () => {
      if (types.isPropAbsent(filterValue)) {
        checkedIndex.value = 0;
        return;
      }
      const idx = (filters.value || []).findIndex((item) => {
        return item.value === filterValue.value;
      });
      checkedIndex.value = idx >= 0 ? idx + 1 : 0;
    };
    const handleKeydown = (event$1) => {
      var _a, _b;
      const code = event.getEventCode(event$1);
      const len = (filters.value ? filters.value.length : 0) + 1;
      let index = checkedIndex.value;
      let isPreventDefault = true;
      switch (code) {
        case aria.EVENT_CODE.down:
        case aria.EVENT_CODE.right:
          index = (index + 1) % len;
          break;
        case aria.EVENT_CODE.up:
        case aria.EVENT_CODE.left:
          index = (index - 1 + len) % len;
          break;
        case aria.EVENT_CODE.tab:
          hidden();
          isPreventDefault = false;
          break;
        case aria.EVENT_CODE.enter:
        case aria.EVENT_CODE.space:
          if (index === 0) {
            handleSelect(null, 0);
          } else {
            const item = (filters.value || [])[index - 1];
            item.value && handleSelect(item.value, index);
          }
          break;
        default:
          isPreventDefault = false;
          break;
      }
      isPreventDefault && event$1.preventDefault();
      checkedIndex.value = index;
      (_b = (_a = rootRef.value) == null ? void 0 : _a.querySelector(
        `.${ns.e("list-item")}:nth-child(${index + 1})`
      )) == null ? void 0 : _b.focus();
    };
    return {
      multiple,
      filterClassName,
      filteredValue,
      filterValue,
      filters,
      handleConfirm,
      handleReset,
      handleSelect,
      isPropAbsent: types.isPropAbsent,
      isActive,
      t,
      ns,
      tooltipRef,
      rootRef,
      checkedIndex,
      handleShowTooltip,
      handleHideTooltip,
      handleKeydown
    };
  }
});
const _hoisted_1 = ["disabled"];
const _hoisted_2 = ["tabindex", "aria-checked"];
const _hoisted_3 = ["tabindex", "aria-checked", "onClick"];
const _hoisted_4 = ["aria-label"];
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_el_checkbox = vue.resolveComponent("el-checkbox");
  const _component_el_checkbox_group = vue.resolveComponent("el-checkbox-group");
  const _component_el_scrollbar = vue.resolveComponent("el-scrollbar");
  const _component_arrow_up = vue.resolveComponent("arrow-up");
  const _component_arrow_down = vue.resolveComponent("arrow-down");
  const _component_el_icon = vue.resolveComponent("el-icon");
  const _component_el_tooltip = vue.resolveComponent("el-tooltip");
  return vue.openBlock(), vue.createBlock(_component_el_tooltip, {
    ref: "tooltipRef",
    offset: 0,
    placement: _ctx.placement,
    "show-arrow": false,
    trigger: "click",
    role: "dialog",
    teleported: "",
    effect: "light",
    pure: "",
    loop: "",
    "popper-class": _ctx.filterClassName,
    persistent: "",
    "append-to": _ctx.appendTo,
    onShow: _ctx.handleShowTooltip,
    onHide: _ctx.handleHideTooltip
  }, {
    content: vue.withCtx(() => [
      _ctx.multiple ? (vue.openBlock(), vue.createElementBlock(
        "div",
        {
          key: 0,
          ref: "rootRef",
          tabindex: "-1",
          class: vue.normalizeClass(_ctx.ns.e("multiple"))
        },
        [
          vue.createElementVNode(
            "div",
            {
              class: vue.normalizeClass(_ctx.ns.e("content"))
            },
            [
              vue.createVNode(_component_el_scrollbar, {
                "wrap-class": _ctx.ns.e("wrap")
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_el_checkbox_group, {
                    modelValue: _ctx.filteredValue,
                    "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => _ctx.filteredValue = $event),
                    class: vue.normalizeClass(_ctx.ns.e("checkbox-group"))
                  }, {
                    default: vue.withCtx(() => [
                      (vue.openBlock(true), vue.createElementBlock(
                        vue.Fragment,
                        null,
                        vue.renderList(_ctx.filters, (filter) => {
                          return vue.openBlock(), vue.createBlock(_component_el_checkbox, {
                            key: filter.value,
                            value: filter.value
                          }, {
                            default: vue.withCtx(() => [
                              vue.createTextVNode(
                                vue.toDisplayString(filter.text),
                                1
                              )
                            ]),
                            _: 2
                          }, 1032, ["value"]);
                        }),
                        128
                      ))
                    ]),
                    _: 1
                  }, 8, ["modelValue", "class"])
                ]),
                _: 1
              }, 8, ["wrap-class"])
            ],
            2
          ),
          vue.createElementVNode(
            "div",
            {
              class: vue.normalizeClass(_ctx.ns.e("bottom"))
            },
            [
              vue.createElementVNode("button", {
                class: vue.normalizeClass(_ctx.ns.is("disabled", _ctx.filteredValue.length === 0)),
                disabled: _ctx.filteredValue.length === 0,
                type: "button",
                onClick: _cache[1] || (_cache[1] = (...args) => _ctx.handleConfirm && _ctx.handleConfirm(...args))
              }, vue.toDisplayString(_ctx.t("el.table.confirmFilter")), 11, _hoisted_1),
              vue.createElementVNode(
                "button",
                {
                  type: "button",
                  onClick: _cache[2] || (_cache[2] = (...args) => _ctx.handleReset && _ctx.handleReset(...args))
                },
                vue.toDisplayString(_ctx.t("el.table.resetFilter")),
                1
              )
            ],
            2
          )
        ],
        2
      )) : (vue.openBlock(), vue.createElementBlock(
        "ul",
        {
          key: 1,
          ref: "rootRef",
          tabindex: "-1",
          role: "radiogroup",
          class: vue.normalizeClass(_ctx.ns.e("list")),
          onKeydown: _cache[4] || (_cache[4] = (...args) => _ctx.handleKeydown && _ctx.handleKeydown(...args))
        },
        [
          vue.createElementVNode("li", {
            role: "radio",
            class: vue.normalizeClass([
              _ctx.ns.e("list-item"),
              _ctx.ns.is("active", _ctx.isPropAbsent(_ctx.filterValue))
            ]),
            tabindex: _ctx.checkedIndex === 0 ? 0 : -1,
            "aria-checked": _ctx.isPropAbsent(_ctx.filterValue),
            onClick: _cache[3] || (_cache[3] = ($event) => _ctx.handleSelect(null, 0))
          }, vue.toDisplayString(_ctx.t("el.table.clearFilter")), 11, _hoisted_2),
          (vue.openBlock(true), vue.createElementBlock(
            vue.Fragment,
            null,
            vue.renderList(_ctx.filters, (filter, idx) => {
              return vue.openBlock(), vue.createElementBlock("li", {
                key: filter.value,
                role: "radio",
                class: vue.normalizeClass([_ctx.ns.e("list-item"), _ctx.ns.is("active", _ctx.isActive(filter))]),
                tabindex: _ctx.checkedIndex === idx + 1 ? 0 : -1,
                "aria-checked": _ctx.isActive(filter),
                onClick: ($event) => _ctx.handleSelect(filter.value, idx + 1)
              }, vue.toDisplayString(filter.text), 11, _hoisted_3);
            }),
            128
          ))
        ],
        34
      ))
    ]),
    default: vue.withCtx(() => {
      var _a;
      return [
        vue.createElementVNode("button", {
          type: "button",
          class: vue.normalizeClass(`${_ctx.ns.namespace.value}-table__column-filter-trigger`),
          "aria-label": _ctx.t("el.table.filterLabel", { column: ((_a = _ctx.column) == null ? void 0 : _a.label) || "" })
        }, [
          vue.createVNode(_component_el_icon, null, {
            default: vue.withCtx(() => [
              vue.renderSlot(_ctx.$slots, "filter-icon", {}, () => {
                var _a2;
                return [
                  ((_a2 = _ctx.column) == null ? void 0 : _a2.filterOpened) ? (vue.openBlock(), vue.createBlock(_component_arrow_up, { key: 0 })) : (vue.openBlock(), vue.createBlock(_component_arrow_down, { key: 1 }))
                ];
              })
            ]),
            _: 3
          })
        ], 10, _hoisted_4)
      ];
    }),
    _: 3
  }, 8, ["placement", "popper-class", "append-to", "onShow", "onHide"]);
}
var FilterPanel = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["render", _sfc_render], ["__file", "/home/runner/work/element-plus/element-plus/packages/components/table/src/filter-panel.vue"]]);

exports["default"] = FilterPanel;
//# sourceMappingURL=filter-panel.js.map
