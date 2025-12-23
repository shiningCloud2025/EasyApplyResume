import { defineComponent, computed, openBlock, createElementBlock, normalizeClass, unref, createElementVNode, renderSlot, toDisplayString, createVNode, withCtx, createTextVNode, createCommentVNode, createSlots, normalizeProps, guardReactiveProps, Fragment, renderList, createBlock, mergeProps } from 'vue';
import { ElButtonGroup, ElButton } from '../../button/index.mjs';
import DateTable from './date-table2.mjs';
import { useCalendar } from './use-calendar.mjs';
import { calendarProps, calendarEmits } from './calendar2.mjs';
import _export_sfc from '../../../_virtual/plugin-vue_export-helper.mjs';
import { useNamespace } from '../../../hooks/use-namespace/index.mjs';
import { useLocale } from '../../../hooks/use-locale/index.mjs';

const COMPONENT_NAME = "ElCalendar";
const _sfc_main = defineComponent({
  ...{
    name: COMPONENT_NAME
  },
  __name: "calendar",
  props: calendarProps,
  emits: calendarEmits,
  setup(__props, { expose: __expose, emit: __emit }) {
    const ns = useNamespace("calendar");
    const props = __props;
    const emit = __emit;
    const {
      calculateValidatedDateRange,
      date,
      pickDay,
      realSelectedDay,
      selectDate,
      validatedRange
    } = useCalendar(props, emit, COMPONENT_NAME);
    const { t } = useLocale();
    const i18nDate = computed(() => {
      const pickedMonth = `el.datepicker.month${date.value.format("M")}`;
      return `${date.value.year()} ${t("el.datepicker.year")} ${t(pickedMonth)}`;
    });
    __expose({
      selectedDay: realSelectedDay,
      pickDay,
      selectDate,
      calculateValidatedDateRange
    });
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(
        "div",
        {
          class: normalizeClass(unref(ns).b())
        },
        [
          createElementVNode(
            "div",
            {
              class: normalizeClass(unref(ns).e("header"))
            },
            [
              renderSlot(_ctx.$slots, "header", { date: i18nDate.value }, () => [
                createElementVNode(
                  "div",
                  {
                    class: normalizeClass(unref(ns).e("title"))
                  },
                  toDisplayString(i18nDate.value),
                  3
                ),
                unref(validatedRange).length === 0 ? (openBlock(), createElementBlock(
                  "div",
                  {
                    key: 0,
                    class: normalizeClass(unref(ns).e("button-group"))
                  },
                  [
                    createVNode(unref(ElButtonGroup), null, {
                      default: withCtx(() => [
                        createVNode(unref(ElButton), {
                          size: "small",
                          onClick: _cache[0] || (_cache[0] = ($event) => unref(selectDate)("prev-month"))
                        }, {
                          default: withCtx(() => [
                            createTextVNode(
                              toDisplayString(unref(t)("el.datepicker.prevMonth")),
                              1
                            )
                          ]),
                          _: 1
                        }),
                        createVNode(unref(ElButton), {
                          size: "small",
                          onClick: _cache[1] || (_cache[1] = ($event) => unref(selectDate)("today"))
                        }, {
                          default: withCtx(() => [
                            createTextVNode(
                              toDisplayString(unref(t)("el.datepicker.today")),
                              1
                            )
                          ]),
                          _: 1
                        }),
                        createVNode(unref(ElButton), {
                          size: "small",
                          onClick: _cache[2] || (_cache[2] = ($event) => unref(selectDate)("next-month"))
                        }, {
                          default: withCtx(() => [
                            createTextVNode(
                              toDisplayString(unref(t)("el.datepicker.nextMonth")),
                              1
                            )
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })
                  ],
                  2
                )) : createCommentVNode("v-if", true)
              ])
            ],
            2
          ),
          unref(validatedRange).length === 0 ? (openBlock(), createElementBlock(
            "div",
            {
              key: 0,
              class: normalizeClass(unref(ns).e("body"))
            },
            [
              createVNode(DateTable, {
                date: unref(date),
                "selected-day": unref(realSelectedDay),
                onPick: unref(pickDay)
              }, createSlots({
                _: 2
              }, [
                _ctx.$slots["date-cell"] ? {
                  name: "date-cell",
                  fn: withCtx((data) => [
                    renderSlot(_ctx.$slots, "date-cell", normalizeProps(guardReactiveProps(data)))
                  ]),
                  key: "0"
                } : void 0
              ]), 1032, ["date", "selected-day", "onPick"])
            ],
            2
          )) : (openBlock(), createElementBlock(
            "div",
            {
              key: 1,
              class: normalizeClass(unref(ns).e("body"))
            },
            [
              (openBlock(true), createElementBlock(
                Fragment,
                null,
                renderList(unref(validatedRange), (range_, index) => {
                  return openBlock(), createBlock(DateTable, {
                    key: index,
                    date: range_[0],
                    "selected-day": unref(realSelectedDay),
                    range: range_,
                    "hide-header": index !== 0,
                    onPick: unref(pickDay)
                  }, createSlots({
                    _: 2
                  }, [
                    _ctx.$slots["date-cell"] ? {
                      name: "date-cell",
                      fn: withCtx((data) => [
                        renderSlot(_ctx.$slots, "date-cell", mergeProps({ ref_for: true }, data))
                      ]),
                      key: "0"
                    } : void 0
                  ]), 1032, ["date", "selected-day", "range", "hide-header", "onPick"]);
                }),
                128
              ))
            ],
            2
          ))
        ],
        2
      );
    };
  }
});
var Calendar = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "/home/runner/work/element-plus/element-plus/packages/components/calendar/src/calendar.vue"]]);

export { Calendar as default };
//# sourceMappingURL=calendar.mjs.map
