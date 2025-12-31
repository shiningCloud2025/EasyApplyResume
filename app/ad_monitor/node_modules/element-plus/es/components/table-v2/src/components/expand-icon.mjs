import { createVNode, mergeProps } from 'vue';
import { ElIcon } from '../../../icon/index.mjs';
import { ArrowRight } from '@element-plus/icons-vue';

const ExpandIcon = (props) => {
  const {
    expanded,
    expandable,
    onExpand,
    style,
    size,
    ariaLabel
  } = props;
  const expandIconProps = {
    onClick: expandable ? () => onExpand(!expanded) : void 0,
    ariaLabel,
    ariaExpanded: expanded,
    class: props.class
  };
  return createVNode("button", mergeProps(expandIconProps, {
    "type": "button"
  }), [createVNode(ElIcon, {
    "size": size,
    "style": style
  }, {
    default: () => [createVNode(ArrowRight, null, null)]
  })]);
};
var ExpandIcon$1 = ExpandIcon;

export { ExpandIcon$1 as default };
//# sourceMappingURL=expand-icon.mjs.map
