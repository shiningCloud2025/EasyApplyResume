"""测试动画是否可用 - 2页简单PPT，每页2个元素点击出现"""
from pptx import Presentation
from pptx.util import Inches, Pt
from lxml import etree
from pptx.enum.text import PP_ALIGN

P = "http://schemas.openxmlformats.org/presentationml/2006/main"
_id = [100]

def nid(): _id[0]+=1; return str(_id[0])

def add_shape_with_id(slide, left, top, width, height, text):
    """添加文本框并获取其 spid"""
    txBox = slide.shapes.add_textbox(Inches(left), Inches(top), Inches(width), Inches(height))
    tf = txBox.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = text
    p.font.size = Pt(24)
    p.font.bold = True
    p.alignment = PP_ALIGN.CENTER
    
    # Get spid from XML
    for nv in txBox._element.iter(f"{{{P}}}nvSpPr"):
        for cn in nv.iter(f"{{{P}}}cNvPr"):
            return txBox, cn.get("id")
    return txBox, None

def add_transition(slide, ttype="fade"):
    trans = etree.Element(f"{{{P}}}transition", spd="med")
    etree.SubElement(trans, f"{{{P}}}fade")
    c = slide.element.find(f"{{{P}}}cSld")
    if c is not None: c.addnext(trans)

def add_click_anim(slide, spid, effect="fade", dur=500):
    se = slide.element
    timing = se.find(f"{{{P}}}timing")
    if timing is None:
        timing = etree.Element(f"{{{P}}}timing")
        tnLst = etree.SubElement(timing, f"{{{P}}}tnLst")
        rp = etree.SubElement(tnLst, f"{{{P}}}par")
        rc = etree.SubElement(rp, f"{{{P}}}cTn", id=nid(), dur="indefinite", restart="never", nodeType="tmRoot")
        rcl = etree.SubElement(rc, f"{{{P}}}childTnLst")
        sq = etree.SubElement(rcl, f"{{{P}}}seq", concurrent="1", nextAc="seek")
        mc = etree.SubElement(sq, f"{{{P}}}cTn", id=nid(), dur="indefinite", nodeType="mainSeq")
        mcl = etree.SubElement(mc, f"{{{P}}}childTnLst")
        cSld = se.find(f"{{{P}}}cSld")
        trans = se.find(f"{{{P}}}transition")
        target = trans if trans is not None else cSld
        if target is not None: target.addnext(timing)
        else: se.append(timing)
    else:
        mcl = timing.find(f"{{{P}}}tnLst/{{{P}}}par/{{{P}}}cTn/{{{P}}}childTnLst/{{{P}}}seq/{{{P}}}cTn/{{{P}}}childTnLst")

    ap = etree.SubElement(mcl, f"{{{P}}}par")
    ac = etree.SubElement(ap, f"{{{P}}}cTn", id=nid(), dur=str(dur), fill="hold")
    stl = etree.SubElement(ac, f"{{{P}}}stCondLst")
    cond = etree.SubElement(stl, f"{{{P}}}cond")
    cond.set("delay", "0")
    acl = etree.SubElement(ac, f"{{{P}}}childTnLst")
    ae = etree.SubElement(acl, f"{{{P}}}animEffect", transition="in", filterType=effect)
    if effect == "zoom":
        ae.set("zoomTransition", "in")
    cb = etree.SubElement(ae, f"{{{P}}}cBhvr")
    etree.SubElement(cb, f"{{{P}}}cTn", id=nid(), dur=str(dur))
    te = etree.SubElement(cb, f"{{{P}}}tgtEl")
    etree.SubElement(te, f"{{{P}}}spTgt", spid=str(spid))

# ===== BUILD TEST PPT =====
prs = Presentation()
prs.slide_width = Inches(10)
prs.slide_height = Inches(5.625)

# Slide 1
s1 = prs.slides.add_slide(prs.slide_layouts[6])  # blank
from pptx.dml.color import RGBColor

bg1 = s1.background
bg1.fill.solid()
bg1.fill.fore_color.rgb = RGBColor(0x0F, 0x17, 0x2A)

_, spid1 = add_shape_with_id(s1, 1, 1.5, 8, 1, "点击鼠标 → 查看第1个特效")
_, spid2 = add_shape_with_id(s1, 1, 3.0, 8, 1, "再点一次 → 第2个特效出现！")

add_transition(s1, "fade")
add_click_anim(s1, spid1, "zoom", 600)
add_click_anim(s1, spid2, "fly", 500)

# Slide 2
s2 = prs.slides.add_slide(prs.slide_layouts[6])
s2.background.fill.solid()
s2.background.fill.fore_color.rgb = RGBColor(0xF1, 0xF5, 0xF9)

_, spid3 = add_shape_with_id(s2, 1, 1.0, 8, 0.8, "测试页2 — 继续点击")
_, spid4 = add_shape_with_id(s2, 1, 2.2, 8, 0.8, "每个元素都是点一次出现一个")
_, spid5 = add_shape_with_id(s2, 1, 3.4, 8, 0.8, "这就是点击触发的进场特效")

add_transition(s2, "push")
add_click_anim(s2, spid3, "wipe", 500)
add_click_anim(s2, spid4, "fly", 500)
add_click_anim(s2, spid5, "zoom", 500)

prs.save("out/test-animation.pptx")
print("Test PPT saved! Open it, press F5, and click to see animations.")

# Verify
prs2 = Presentation("out/test-animation.pptx")
for i, s in enumerate(prs2.slides):
    tm = s.element.find(f"{{{P}}}timing")
    tr = s.element.find(f"{{{P}}}transition")
    print(f"  Slide {i+1}: trans={'YES' if tr is not None else 'NO'}, anim={'YES' if tm is not None else 'NO'}")
    if tm is not None:
        print(f"    Timing XML sample: {etree.tostring(tm, encoding='unicode')[:300]}")
