"""
用 PowerPoint COM 创建一个带 Appear 入场动画的 PPTX
然后用 lxml 读取其真实的动画 XML 结构
"""
import win32com.client
import time, os

ppt = win32com.client.Dispatch("PowerPoint.Application")
try:
    ppt.Visible = False
except:
    pass  # Office 2007 might not support this

pres = ppt.Presentations.Add()

# Slide 1: 白色背景 + 3个色块，每个带点击入场动画
slide = pres.Slides.Add(1, 12)  # 12 = ppLayoutBlank

# 添加3个矩形
shapes_data = []
colors = [(2,132,199), (13,148,143), (234,88,12)]
for i in range(3):
    sh = slide.Shapes.AddShape(1, 200 + i*350, 300, 300, 250)  # 1=msoShapeRectangle
    sh.Fill.ForeColor.RGB = colors[i][0] + colors[i][1]*256 + colors[i][2]*65536
    tf = sh.TextFrame
    tf.TextRange.Text = f"Box {i+1}"
    tf.TextRange.Font.Size = 20
    tf.TextRange.Font.Color.RGB = 0xFFFFFF
    tf.TextRange.ParagraphFormat.Alignment = 2  # ppAlignCenter

# 添加点击触发的 Entrance Appear 动画（使用 PowerPoint 原生方式）
for i in range(1, 4):  # shapes 1,2,3
    sh = slide.Shapes(i)
    # 方法1: TimeLine.MainSequence.AddEffect
    seq = slide.TimeLine.MainSequence
    effect = seq.AddEffect(sh, 1, 1, 1)  # msoAnimEffectAppear, msoAnimateLevel=msoAnimateByFirstLevel, msoAnimTriggerAfterPrevious... let me check
    # Actually: AddEffect(Shape, effectId, level, trigger, index)
    # trigger: 1=msoAnimTriggerOnPageClick, 2=msoAnimTriggerOnShapeClick, 3=msoAnimTriggerAfterPrevious, 4=msoAnimTriggerWithPrevious
    effect.Timing.TriggerType = 1  # msoAnimTriggerOnPageClick

out_path = os.path.abspath("out/native-anim.pptx")
pres.SaveAs(out_path)
pres.Close()
ppt.Quit()

print(f"Saved: {out_path}")

# ============ 读取 XML 结构 ============
import zipfile
from lxml import etree

with zipfile.ZipFile(out_path, 'r') as z:
    with z.open('ppt/slides/slide1.xml') as f:
        sld = etree.fromstring(f.read())

P = 'http://schemas.openxmlformats.org/presentationml/2006/main'
A = 'http://schemas.openxmlformats.org/drawingml/2006/main'

timing = sld.find(f'{{{P}}}timing')
if timing is not None:
    # 打印第一个 par
    pars = timing.findall(f'.//{{{P}}}par')
    print(f"\nTotal anim pars: {len(pars)}")
    print("\n=== FULL First par XML (reference) ===")
    print(etree.tostring(pars[0], pretty_print=True, encoding='unicode'))
else:
    print("NO TIMING found!")
    # 打印 sld 的子元素
    print("\nSlide child elements:")
    for c in sld:
        print(f"  {c.tag.split('}')[-1]}")
