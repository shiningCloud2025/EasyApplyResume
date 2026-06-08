"""
最小测试：验证 animEffect 格式的动画是否生效
"""
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN
from lxml import etree
import uuid

P_NS = 'http://schemas.openxmlformats.org/presentationml/2006/main'

prs = Presentation()
prs.slide_width = Inches(13.333)
prs.slide_height = Inches(7.5)

s = prs.slides.add_slide(prs.slide_layouts[6])
s.background.fill.solid()
s.background.fill.fore_color.rgb = RGBColor(0xFF, 0xFF, 0xFF)

# 标题
t1 = s.shapes.add_textbox(Inches(1), Inches(0.5), Inches(11), Inches(0.8))
t1.text_frame.paragraphs[0].text = "测试：点击鼠标看色块逐个出现"
t1.text_frame.paragraphs[0].font.size = Pt(24)
t1.text_frame.paragraphs[0].font.color.rgb = RGBColor(0x0F, 0x17, 0x2A)
t1.text_frame.paragraphs[0].font.bold = True

# 3个色块
colors = [(0x02,0x84,0xC7), (0x0D,0x94,0x8F), (0xEA,0x58,0x0C)]
spids = []
for i, (r,g,b) in enumerate(colors):
    sh = s.shapes.add_shape(1, Inches(2+i*3.5), Inches(2.5), Inches(3), Inches(2.5))
    sh.fill.solid()
    sh.fill.fore_color.rgb = RGBColor(r, g, b)
    sh.line.fill.background()
    tf = sh.text_frame
    tf.paragraphs[0].text = f"色块 {i+1}"
    tf.paragraphs[0].font.size = Pt(20)
    tf.paragraphs[0].font.color.rgb = RGBColor(0xFF,0xFF,0xFF)
    tf.paragraphs[0].alignment = PP_ALIGN.CENTER
    spids.append(sh.shape_id)

# ============ 用 animEffect 格式注入动画 ============

# 翻页过渡
trans_el = etree.Element(f"{{{P_NS}}}transition")
trans_el.set("spd", "med")
fade = etree.SubElement(trans_el, f"{{{P_NS}}}fade")
s.element.append(trans_el)

# 元素动画
timing = etree.Element(f"{{{P_NS}}}timing")
tnLst = etree.SubElement(timing, f"{{{P_NS}}}tnLst")
seq = etree.SubElement(tnLst, f"{{{P_NS}}}seq")
seq.set("concurrent", "0")

cTn_root = etree.SubElement(seq, f"{{{P_NS}}}cTn")
cTn_root.set("id", str(uuid.uuid4()))
cTn_root.set("dur", "indefinite")
scl = etree.SubElement(cTn_root, f"{{{P_NS}}}stCondLst")
c0 = etree.SubElement(scl, f"{{{P_NS}}}cond")
c0.set("delay", "0")

childTnLst = etree.SubElement(seq, f"{{{P_NS}}}childTnLst")

for spid in spids:
    par = etree.SubElement(childTnLst, f"{{{P_NS}}}par")
    
    ct = etree.SubElement(par, f"{{{P_NS}}}cTn")
    ct.set("id", str(uuid.uuid4()))
    ct.set("fill", "hold")
    stc = etree.SubElement(ct, f"{{{P_NS}}}stCondLst")
    cond = etree.SubElement(stc, f"{{{P_NS}}}cond")
    cond.set("evt", "onclick")
    cond.set("delay", "0")
    te = etree.SubElement(cond, f"{{{P_NS}}}tgtEl")
    etree.SubElement(te, f"{{{P_NS}}}sldTgt")
    
    cl = etree.SubElement(par, f"{{{P_NS}}}childTnLst")
    
    # animEffect - PowerPoint 标准入场动画
    ae = etree.SubElement(cl, f"{{{P_NS}}}animEffect")
    ae.set("transition", "in")
    ae.set("filter", "appear")
    
    cb = etree.SubElement(ae, f"{{{P_NS}}}cBhvr")
    cbt = etree.SubElement(cb, f"{{{P_NS}}}cTn")
    cbt.set("id", str(uuid.uuid4()))
    cbt.set("dur", "500")
    
    tgt = etree.SubElement(cb, f"{{{P_NS}}}tgtEl")
    st = etree.SubElement(tgt, f"{{{P_NS}}}spTgt")
    st.set("spid", str(spid))

s.element.append(timing)

out = "out/test-anim-v3.pptx"
prs.save(out)

# 验证
from lxml import etree as etree2
prs2 = Presentation(out)
for i, sl in enumerate(prs2.slides):
    tm = sl.element.find(f'{{{P_NS}}}timing')
    if tm is not None:
        pars = tm.findall(f'.//{{{P_NS}}}par')
        has_ae = len(tm.findall(f'.//{{{P_NS}}}animEffect')) > 0
        has_click = any(c.get('evt')=='onclick' for c in tm.findall(f'.//{{{P_NS}}}cond'))
        print(f"Slide {i+1}: anims={len(pars)}, animEffect={has_ae}, onclick={has_click}")
    else:
        print(f"Slide {i+1}: NO timing")

print(f"\nSaved: {out}")
print("The XML uses animEffect format (PowerPoint's native entrance animation)")
p0 = prs2.slides[0].element.find(f'{{{P_NS}}}timing')
par = p0.findall(f'.//{{{P_NS}}}par')[0]
print("\nFirst animation XML:")
print(etree2.tostring(par, pretty_print=True, encoding='unicode'))
