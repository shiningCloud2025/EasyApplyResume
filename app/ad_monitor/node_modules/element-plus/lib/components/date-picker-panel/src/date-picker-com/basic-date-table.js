'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var vue = require('vue');
var basicDateTable = require('../props/basic-date-table.js');
var useBasicDateTable = require('../composables/use-basic-date-table.js');
var basicCellRender = require('./basic-cell-render.js');
var pluginVue_exportHelper = require('../../../../_virtual/plugin-vue_export-helper.js');

const _hoisted_1 = ["aria-label"];
const _hoisted_2 = ["aria-label"];
const _hoisted_3 = ["aria-current", "aria-selected", "tabindex", "aria-disabled"];
const _sfc_main = vue.defineComponent({
  __name: "basic-date-table",
  props: basicDateTable.basicDateTableProps,
  emits: basicDateTable.basicDateTableEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const props = __props;
    const emit = __emit;
    const {
      WEEKS,
      rows,
      tbodyRef,
      currentCellRef,
      focus,
      isCurrent,
      isWeekActive,
      isSelectedCell,
      handlePickDate,
      handleMouseUp,
      handleMouseDown,
      handleMouseMove,
      handleFocus
    } = useBasicDateTable.useBasicDateTable(props, emit);
    const { tableLabel, tableKls, getCellClasses, getRowKls, weekHeaderClass, t } = useBasicDateTable.useBasicDateTableDOM(props, {
      isCurrent,
      isWeekActive
    });
    let isUnmounting = false;
    vue.onBeforeUnmount(() => {
      isUnmounting = true;
    });
    __expose({
      focus
    });
    return (_ctx, _cache) => {
      return vue.openBlock(), vue.createElementBlock("table", {
        "aria-label": vue.unref(tableLabel),
        class: vue.normalizeClass(vue.unref(tableKls)),
        cellspacing: "0",
        cellpadding: "0",
        role: "grid",
        onClick: _cache[1] || (_cache[1] = (...args) => vue.unref(handlePickDate) && vue.unref(handlePickDate)(...args)),
        onMousemove: _cache[2] || (_cache[2] = (...args) => vue.unref(handleMouseMove) && vue.unref(handleMouseMove)(...args)),
        onMousedown: _cache[3] || (_cache[3] = (...args) => vue.unref(handleMouseDown) && vue.unref(handleMouseDown)(...args)),
        onMouseup: _cache[4] || (_cache[4] = (...args) => vue.unref(handleMouseUp) && vue.unref(handleMouseUp)(...args))
      }, [
        vue.createElementVNode(
          "tbody",
          {
            ref_key: "tbodyRef",
            ref: tbodyRef
          },
          [
            vue.createElementVNode("tr", null, [
              _ctx.showWeekNumber ? (vue.openBlock(), vue.createElementBlock(
                "th",
                {
                  key: 0,
                  scope: "col",
                  class: vue.normalizeClass(vue.unref(weekHeaderClass))
                },
                null,
                2
              )) : vue.createCommentVNode("v-if", true),
              (vue.openBlock(true), vue.createElementBlock(
                vue.Fragment,
                null,
                vue.renderList(vue.unref(WEEKS), (week, key) => {
                  return vue.openBlock(), vue.createElementBlock("th", {
                    key,
                    "aria-label": vue.unref(t)("el.datepicker.weeksFull." + week),
                    scope: "col"
                  }, vue.toDisplayString(vue.unref(t)("el.datepicker.weeks." + week)), 9, _hoisted_2);
                }),
                128
              ))
            ]),
            (vue.openBlock(true), vue.createElementBlock(
              vue.Fragment,
              null,
              vue.renderList(vue.unref(rows), (row, rowKey) => {
                return vue.openBlock(), vue.createElementBlock(
                  "tr",
                  {
                    key: rowKey,
                    class: vue.normalizeClass(vue.unref(getRowKls)(_ctx.showWeekNumber ? row[2] : row[1]))
                  },
                  [
                    (vue.openBlock(true), vue.createElementBlock(
                      vue.Fragment,
                      null,
                      vue.renderList(row, (cell, columnKey) => {
                        return vue.openBlock(), vue.createElementBlock("td", {
                          key: `${rowKey}.${columnKey}`,
                          ref_for: true,
                          ref: (el) => !vue.unref(isUnmounting) && vue.unref(isSelectedCell)(cell) && (currentCellRef.value = el),
                          class: vue.normalizeClass(vue.unref(getCellClasses)(cell)),
                          "aria-current": cell.isCurrent ? "date" : void 0,
                          "aria-selected": cell.isCurrent,
                          tabindex: _ctx.disabled ? void 0 : vue.unref(isSelectedCell)(cell) ? 0 : -1,
                          "aria-disabled": _ctx.disabled,
                          onFocus: _cache[0] || (_cache[0] = (...args) => vue.unref(handleFocus) && vue.unref(handleFocus)(...args))
                        }, [
                          vue.createVNode(vue.unref(basicCellRender["default"]), { cell }, null, 8, ["cell"])
                        ], 42, _hoisted_3);
                      }),
                      128
                    ))
                  ],
                  2
                );
              }),
              128
            ))
          ],
          512
        )
      ], 42, _hoisted_1);
    };
  }
});
var DateTable = /* @__PURE__ */ pluginVue_exportHelper["default"](_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/date-picker-panel/src/date-picker-com/basic-date-table.vue"]]);

exports["default"] = DateTable;
//# sourceMappingURL=basic-date-table.js.map
