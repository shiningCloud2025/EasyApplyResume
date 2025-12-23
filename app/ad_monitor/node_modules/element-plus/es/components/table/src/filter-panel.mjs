import { defineComponent, getCurrentInstance, ref, computed, resolveComponent, openBlock, createBlock, withCtx, createElementBlock, normalizeClass, createElementVNode, createVNode, Fragment, renderList, createTextVNode, toDisplayString, renderSlot } from 'vue';
import { ElCheckbox, ElCheckboxGroup } from '../../checkbox/index.mjs';
import { ElIcon } from '../../icon/index.mjs';
import { ArrowDown, ArrowUp } from '@element-plus/icons-vue';
import { ElTooltip } from '../../tooltip/index.mjs';
import { ElScrollbar } from '../../scrollbar/index.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useTooltipContentProps } from '../../tooltip/src/content.mjs';
import { useLocale } from '../../../hooks/use-locale/index.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { isPropAbsent } from '../../../utils/types.mjs';
import { getEventCode } from '../../../utils/dom/event.mjs';
import { EVENT_CODE } from '../../../constants/aria.mjs';

const _sfc_main = defineComponent({
  name: "ElTableFilterPanel",
  components: {
    ElCheckbox,
    ElCheckboxGroup,
    ElScrollbar,
    ElTooltip,
    ElIcon,
    ArrowDown,
    ArrowUp
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
    appendTo: useTooltipContentProps.appendTo
  },
  setup(props) {
    const instance = getCurrentInstance();
    const { t } = useLocale();
    const ns = useNamespace("table-filter");
    const parent = instance == null ? void 0 : instance.parent;
    if (props.column && !parent.filterPanels.value[props.column.id]) {
      parent.filterPanels.value[props.column.id] = instance;
    }
    const tooltipRef = ref(null);
    const rootRef = ref(null);
    const checkedIndex = ref(0);
    const filters = computed(() => {
      return props.column && props.column.filters;
    });
    const filterClassName = computed(() => {
      if (props.column && props.column.filterClassName) {
        return `${ns.b()} ${props.column.filterClassName}`;
      }
      return ns.b();
    });
    const filterValue = computed({
      get: () => {
        var _a;
        return (((_a = props.column) == null ? void 0 : _a.filteredValue) || [])[0];
      },
      set: (value) => {
        if (filteredValue.value) {
          if (!isPropAbsent(value)) {
            filteredValue.value.splice(0, 1, value);
          } else {
            filteredValue.value.splice(0, 1);
          }
        }
      }
    });
    const filteredValue = computed({
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
    const multiple = computed(() => {
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
      if (!isPropAbsent(_filterValue)) {
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
      if (isPropAbsent(filterValue)) {
        checkedIndex.value = 0;
        return;
      }
      const idx = (filters.value || []).findIndex((item) => {
        return item.value === filterValue.value;
      });
      checkedIndex.value = idx >= 0 ? idx + 1 : 0;
    };
    const handleKeydown = (event) => {
      var _a, _b;
      const code = getEventCode(event);
      const len = (filters.value ? filters.value.length : 0) + 1;
      let index = checkedIndex.value;
      let isPreventDefault = true;
      switch (code) {
        case EVENT_CODE.down:
        case EVENT_CODE.right:
          index = (index + 1) % len;
          break;
        case EVENT_CODE.up:
        case EVENT_CODE.left:
          index = (index - 1 + len) % len;
          break;
        case EVENT_CODE.tab:
          hidden();
          isPreventDefault = false;
          break;
        case EVENT_CODE.enter:
        case EVENT_CODE.space:
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
      isPreventDefault && event.preventDefault();
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
      isPropAbsent,
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
  const _component_el_checkbox = resolveComponent("el-checkbox");
  const _component_el_checkbox_group = resolveComponent("el-checkbox-group");
  const _component_el_scrollbar = resolveComponent("el-scrollbar");
  const _component_arrow_up = resolveComponent("arrow-up");
  const _component_arrow_down = resolveComponent("arrow-down");
  const _component_el_icon = resolveComponent("el-icon");
  const _component_el_tooltip = resolveComponent("el-tooltip");
  return openBlock(), createBlock(_component_el_tooltip, {
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
    content: withCtx(() => [
      _ctx.multiple ? (openBlock(), createElementBlock(
        "div",
        {
          key: 0,
          ref: "rootRef",
          tabindex: "-1",
          class: normalizeClass(_ctx.ns.e("multiple"))
        },
        [
          createElementVNode(
            "div",
            {
              class: normalizeClass(_ctx.ns.e("content"))
            },
            [
              createVNode(_component_el_scrollbar, {
                "wrap-class": _ctx.ns.e("wrap")
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_checkbox_group, {
                    modelValue: _ctx.filteredValue,
                    "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => _ctx.filteredValue = $event),
                    class: normalizeClass(_ctx.ns.e("checkbox-group"))
                  }, {
                    default: withCtx(() => [
                      (openBlock(true), createElementBlock(
                        Fragment,
                        null,
                        renderList(_ctx.filters, (filter) => {
                          return openBlock(), createBlock(_component_el_checkbox, {
                            key: filter.value,
                            value: filter.value
                          }, {
                            default: withCtx(() => [
                              createTextVNode(
                                toDisplayString(filter.text),
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
          createElementVNode(
            "div",
            {
              class: normalizeClass(_ctx.ns.e("bottom"))
            },
            [
              createElementVNode("button", {
                class: normalizeClass(_ctx.ns.is("disabled", _ctx.filteredValue.length === 0)),
                disabled: _ctx.filteredValue.length === 0,
                type: "button",
                onClick: _cache[1] || (_cache[1] = (...args) => _ctx.handleConfirm && _ctx.handleConfirm(...args))
              }, toDisplayString(_ctx.t("el.table.confirmFilter")), 11, _hoisted_1),
              createElementVNode(
                "button",
                {
                  type: "button",
                  onClick: _cache[2] || (_cache[2] = (...args) => _ctx.handleReset && _ctx.handleReset(...args))
                },
                toDisplayString(_ctx.t("el.table.resetFilter")),
                1
              )
            ],
            2
          )
        ],
        2
      )) : (openBlock(), createElementBlock(
        "ul",
        {
          key: 1,
          ref: "rootRef",
          tabindex: "-1",
          role: "radiogroup",
          class: normalizeClass(_ctx.ns.e("list")),
          onKeydown: _cache[4] || (_cache[4] = (...args) => _ctx.handleKeydown && _ctx.handleKeydown(...args))
        },
        [
          createElementVNode("li", {
            role: "radio",
            class: normalizeClass([
              _ctx.ns.e("list-item"),
              _ctx.ns.is("active", _ctx.isPropAbsent(_ctx.filterValue))
            ]),
            tabindex: _ctx.checkedIndex === 0 ? 0 : -1,
            "aria-checked": _ctx.isPropAbsent(_ctx.filterValue),
            onClick: _cache[3] || (_cache[3] = ($event) => _ctx.handleSelect(null, 0))
          }, toDisplayString(_ctx.t("el.table.clearFilter")), 11, _hoisted_2),
          (openBlock(true), createElementBlock(
            Fragment,
            null,
            renderList(_ctx.filters, (filter, idx) => {
              return openBlock(), createElementBlock("li", {
                key: filter.value,
                role: "radio",
                class: normalizeClass([_ctx.ns.e("list-item"), _ctx.ns.is("active", _ctx.isActive(filter))]),
                tabindex: _ctx.checkedIndex === idx + 1 ? 0 : -1,
                "aria-checked": _ctx.isActive(filter),
                onClick: ($event) => _ctx.handleSelect(filter.value, idx + 1)
              }, toDisplayString(filter.text), 11, _hoisted_3);
            }),
            128
          ))
        ],
        34
      ))
    ]),
    default: withCtx(() => {
      var _a;
      return [
        createElementVNode("button", {
          type: "button",
          class: normalizeClass(`${_ctx.ns.namespace.value}-table__column-filter-trigger`),
          "aria-label": _ctx.t("el.table.filterLabel", { column: ((_a = _ctx.column) == null ? void 0 : _a.label) || "" })
        }, [
          createVNode(_component_el_icon, null, {
            default: withCtx(() => [
              renderSlot(_ctx.$slots, "filter-icon", {}, () => {
                var _a2;
                return [
                  ((_a2 = _ctx.column) == null ? void 0 : _a2.filterOpened) ? (openBlock(), createBlock(_component_arrow_up, { key: 0 })) : (openBlock(), createBlock(_component_arrow_down, { key: 1 }))
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
var FilterPanel = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render], ["__file", "/home/runner/work/element-plus/element-plus/packages/components/table/src/filter-panel.vue"]]);

export { FilterPanel as default };
//# sourceMappingURL=filter-panel.mjs.map
