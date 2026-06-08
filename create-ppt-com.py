"""
用 PowerPoint COM 直接生成完整 PPT（动画由 PowerPoint 自己管理，100%可靠）
"""
import win32com.client
import os

ppt = win32com.client.Dispatch("PowerPoint.Application")
try: ppt.Visible = False
except: pass

pres = ppt.Presentations.Add()

# 颜色 (BGR)
BLUE = 0x02 + 0x84*256 + 0xC7*65536
TEAL = 0x0D + 0x94*256 + 0x8F*65536
ORANGE = 0xEA + 0x58*256 + 0x0C*65536
PURPLE = 0x7C + 0x3A*256 + 0xED*65536
DARK_BG = 0x0F + 0x17*256 + 0x2A*65536
WHITE = 0xFFFFFF
BLACK = 0x000000
GRAY = 0x64 + 0x74*256 + 0x8B*65536
TEXT_COLOR = 0x1E + 0x29*256 + 0x3B*65536
CARD_BG = 0xFFFFFF

LIGHT_BG = 0xF1 + 0xF5*256 + 0xF9*65536

pLeft = lambda inch: int(inch * 72 * 12700 / 72)
pTop = lambda inch: int(inch * 72 * 12700 / 72)
pWidth = lambda inch: int(inch * 72 * 12700 / 72)
pHeight = lambda inch: int(inch * 72 * 12700 / 72)

def add_box(slide, x, y, w, h, fill_color, text="", font_size=12, font_color=WHITE, is_round=False):
    shape_type = 14 if is_round else 1  # 14=rounded rect, 1=rect
    sh = slide.Shapes.AddShape(shape_type, pLeft(x), pTop(y), pWidth(w), pHeight(h))
    sh.Fill.ForeColor.RGB = fill_color
    sh.Line.Visible = False
    if text:
        tf = sh.TextFrame
        tf.TextRange.Text = text
        tf.TextRange.Font.Size = font_size
        tf.TextRange.Font.Color.RGB = font_color
        tf.TextRange.ParagraphFormat.Alignment = 2  # center
    return sh

def add_textbox(slide, x, y, w, h, text, font_size=12, font_color=BLACK, bold=False, font_name="Calibri", align=1):
    sh = slide.Shapes.AddTextbox(1, pLeft(x), pTop(y), pWidth(w), pHeight(h))  # 1=msoTextOrientationHorizontal
    tf = sh.TextFrame
    tf.TextRange.Text = text
    tf.TextRange.Font.Size = font_size
    tf.TextRange.Font.Color.RGB = font_color
    tf.TextRange.Font.Bold = bold
    tf.TextRange.Font.Name = font_name
    tf.TextRange.ParagraphFormat.Alignment = align  # 1=left, 2=center, 3=right
    tf.WordWrap = True
    return sh

def add_anim(slide, shape, trigger=1):
    """trigger: 1=onClick"""
    seq = slide.TimeLine.MainSequence
    effect = seq.AddEffect(shape, 1, 0, 1)  # 1=msoAnimEffectAppear
    effect.Timing.TriggerType = trigger

def add_slide_bg(slide, color):
    slide.FollowMasterBackground = False
    slide.Background.Fill.ForeColor.RGB = color

def add_title_bar(slide, text):
    add_box(slide, 0, 0, 13.333, 0.8, DARK_BG)
    add_textbox(slide, 0.6, 0.15, 12, 0.5, text, font_size=22, font_color=WHITE, bold=True, font_name="Trebuchet MS")
    add_box(slide, 0, 0.8, 13.333, 0.03, BLUE)

def add_page_num(slide, n):
    add_textbox(slide, 11.5, 7.1, 1.5, 0.3, f"{n}/9", font_size=9, font_color=GRAY, align=3)

# ============ 幻灯片 1: 封面 ============
s1 = pres.Slides.Add(1, 12)
add_slide_bg(s1, LIGHT_BG)
add_box(s1, 1, 2.0, 0.06, 2.5, BLUE)
add_textbox(s1, 1.5, 2.0, 10, 1.0, "EasyApplyResume", font_size=52, font_color=TEXT_COLOR, bold=True, font_name="Trebuchet MS")
add_textbox(s1, 1.5, 3.2, 10, 0.7, "AI-Driven Smart Job Platform", font_size=24, font_color=BLUE, font_name="Trebuchet MS")
add_box(s1, 1.5, 4.2, 5, 0.02, TEAL)
add_textbox(s1, 1.5, 4.5, 10, 0.5, "Spring Boot . Spring AI . MyBatis-Plus . Redis . PostgreSQL . React . Docker", font_size=13, font_color=GRAY)
for i in range(1, s1.Shapes.Count+1):
    add_anim(s1, s1.Shapes(i))

# ============ 幻灯片 2: 问题背景 ============
s2 = pres.Slides.Add(2, 12)
add_slide_bg(s2, LIGHT_BG)
add_title_bar(s2, "Problem Background")
add_page_num(s2, 2)
problems = [
    ("Resume Making Inefficient", "Mass templates hard to choose\nManual formatting time-consuming", BLUE),
    ("Info Asymmetry", "Unfamiliar with enterprise needs\nLow JD-resume match rate", TEAL),
    ("High Operation Burden", "Mass data manual management\nLack of intelligent tools", ORANGE),
    ("Lack Personalization", "Generic templates not industry-fit\nMissing AI optimization", PURPLE),
]
for i, (ti, de, ac) in enumerate(problems):
    x = 0.6 + (i%2)*6.3; y = 1.2 + (i//2)*2.85
    add_box(s2, x, y, 5.8, 2.4, CARD_BG, is_round=True)
    add_box(s2, x, y, 0.06, 2.4, ac)
    add_textbox(s2, x+0.3, y+0.2, 5.2, 0.45, ti, font_size=18, font_color=ac, bold=True, font_name="Trebuchet MS")
    add_textbox(s2, x+0.3, y+0.85, 5.2, 1.3, de, font_size=14, font_color=TEXT_COLOR)
for i in range(1, s2.Shapes.Count+1):
    add_anim(s2, s2.Shapes(i))

# ============ 幻灯片 3: 技术选型 ============
s3 = pres.Slides.Add(3, 12)
add_slide_bg(s3, LIGHT_BG); add_title_bar(s3, "Tech Stack"); add_page_num(s3, 3)
cats = [
    ("Foundation", ["Spring Boot 3.3.5 + Java 21","Spring Security Dual Chain","MyBatis-Plus 3.5.7","Nacos Config Center"], BLUE),
    ("AI & LLM", ["Spring AI Multi-Model","ReAct Agent Architecture","RAG Retrieval Augmented","DashScope / Zhipu / Ollama"], TEAL),
    ("Data & Storage", ["MySQL + PostgreSQL / PgVector","Redis Cache & Messaging","MinIO / Qiniu OSS","Docker Deployment"], ORANGE),
]
for ci, (cat, items, ac) in enumerate(cats):
    x = 0.6 + ci*4.1
    add_box(s3, x, 1.2, 3.8, 0.5, ac)
    add_textbox(s3, x+0.1, 1.2, 3.6, 0.5, cat, font_size=15, font_color=WHITE, bold=True, font_name="Trebuchet MS", align=2)
    for j, item in enumerate(items):
        iy = 2.0 + j*0.55
        add_box(s3, x, iy, 3.8, 0.45, CARD_BG, is_round=True)
        add_textbox(s3, x+0.15, iy+0.08, 3.5, 0.3, item, font_size=11, font_color=TEXT_COLOR)
for i in range(1, s3.Shapes.Count+1):
    add_anim(s3, s3.Shapes(i))

# ============ 幻灯片 4: AI Agent 架构 ============
s4 = pres.Slides.Add(4, 12)
add_slide_bg(s4, LIGHT_BG); add_title_bar(s4, "AI Agent Architecture"); add_page_num(s4, 4)
agents = [
    ("BaseAgent", "LLM Call . Memory . Base Abstraction", BLUE),
    ("ReActAgent", "Think -> Act -> Observe Cycle", TEAL),
    ("ToolCallAgent", "Tool Registry . Function Calling", ORANGE),
]
for i, (nm, ds, ac) in enumerate(agents):
    y = 1.2 + i*1.5
    add_box(s4, 0.6, y, 5.5, 1.2, CARD_BG, is_round=True)
    add_box(s4, 0.6, y, 0.06, 1.2, ac)
    add_textbox(s4, 1.0, y+0.15, 5, 0.4, nm, font_size=20, font_color=ac, bold=True, font_name="Trebuchet MS")
    add_textbox(s4, 1.0, y+0.6, 5, 0.5, ds, font_size=12, font_color=GRAY)
    if i<2: add_textbox(s4, 3, y+1.15, 1, 0.3, "v", font_size=16, font_color=GRAY, align=2)
add_textbox(s4, 6.8, 1.2, 6, 0.5, "Core Components", font_size=18, font_color=TEXT_COLOR, bold=True, font_name="Trebuchet MS")
comps = [("RAG Knowledge Base","PgVector + Cloud KB",BLUE),("Tool Calling","Web Search . Terminal . File . PDF",TEAL),
         ("Chat Memory","MySQL Persist + In-Memory",ORANGE),("Security Filter","Sensitive Words . Auth",PURPLE),
         ("MCP Client","Multi-Protocol Integration",BLUE),("Stream Output","SSE Real-Time Response",TEAL)]
for i, (lb, ds, ac) in enumerate(comps):
    y=1.85+i*0.82; add_box(s4,6.8,y,6,0.7,CARD_BG,is_round=True); add_box(s4,6.8,y,0.06,0.7,ac)
    add_textbox(s4,7.1,y+0.05,5.4,0.3,lb,font_size=14,font_color=ac,bold=True,font_name="Trebuchet MS")
    add_textbox(s4,7.1,y+0.38,5.4,0.3,ds,font_size=11,font_color=GRAY)
for i in range(1, s4.Shapes.Count+1):
    add_anim(s4, s4.Shapes(i))

# ============ 幻灯片 5: C端 ============
s5 = pres.Slides.Add(5, 12)
add_slide_bg(s5, LIGHT_BG); add_title_bar(s5, "C-End . Resume AI Assistant"); add_page_num(s5, 5)
feats = [("Smart Polish","AI analyzes & optimizes\nAuto adjust wording & layout",BLUE),
         ("Custom Advice","Target JD-based\nSpecific modification plan",TEAL),
         ("RAG Enhanced","Vector DB matching\nPrecise industry knowledge",ORANGE),
         ("Tool Calling","Web Search . PDF . Email . File",PURPLE),
         ("Stream Chat","SSE real-time response\nTypewriter effect",BLUE),
         ("Eco Services","MinIO/Qiniu storage\nMulti-industry templates",TEAL)]
for i,(ti,de,ac) in enumerate(feats):
    x=0.6+(i%3)*4.15; y=1.2+(i//3)*2.85
    add_box(s5,x,y,3.8,2.45,CARD_BG,is_round=True); add_box(s5,x,y,3.8,0.05,ac)
    add_textbox(s5,x+0.2,y+0.25,3.4,0.45,ti,font_size=16,font_color=ac,bold=True,font_name="Trebuchet MS")
    add_textbox(s5,x+0.2,y+0.9,3.4,1.3,de,font_size=12,font_color=TEXT_COLOR)
for i in range(1, s5.Shapes.Count+1):
    add_anim(s5, s5.Shapes(i))

# ============ 幻灯片 6: B端 ============
s6 = pres.Slides.Add(6, 12)
add_slide_bg(s6, LIGHT_BG); add_title_bar(s6, "B-End . System Manager"); add_page_num(s6, 6)
mods = [("User Management","Register/Login . Permission . Role"),("Resume Template","Template CRUD . Enable/Disable"),
        ("Written Test","Question CRUD . Category . Level"),("FAQ Management","Question maintenance . Review"),
        ("User Guide","Manual editing . Rich text")]
for i,(ti,de) in enumerate(mods):
    y=1.15+i*1.12;add_box(s6,0.6,y,6.5,0.95,CARD_BG,is_round=True);add_box(s6,0.6,y,0.06,0.95,BLUE)
    add_textbox(s6,1.0,y+0.1,5.8,0.3,ti,font_size=16,font_color=BLUE,bold=True,font_name="Trebuchet MS")
    add_textbox(s6,1.0,y+0.45,5.8,0.4,de,font_size=11,font_color=GRAY)
add_textbox(s6,7.6,1.15,5.5,0.45,"AI Management Capabilities",font_size=18,font_color=TEAL,bold=True,font_name="Trebuchet MS")
for i,item in enumerate(["Smart Data Statistics & Analysis","Automated Operation Suggestions","Anomaly Detection & Alert",
    "Smart Q&A Assistant","Content Auto Review","Batch Operation Automation","Log Analysis & Diagnosis"]):
    add_textbox(s6,7.6,1.8+i*0.5,5.5,0.4,f"+ {item}",font_size=13,font_color=TEXT_COLOR)
for i in range(1, s6.Shapes.Count+1):
    add_anim(s6, s6.Shapes(i))

# ============ 幻灯片 7: D端 ============
s7 = pres.Slides.Add(7, 12)
add_slide_bg(s7, LIGHT_BG); add_title_bar(s7, "D-End . Data & Monitoring"); add_page_num(s7, 7)
dfs = [("Real-time Monitoring","DAU/Total PV tracking\nMulti-dim dashboard",BLUE),
       ("Ad Management","Ad scheduling & control\nPerformance tracking",TEAL),
       ("Server Monitor","SSH remote management\nHealth check & alert",ORANGE),
       ("Smart Reports","Prometheus metrics\nGrafana dashboard",PURPLE)]
for i,(ti,de,ac) in enumerate(dfs):
    x=0.6+(i%2)*6.3;y=1.2+(i//2)*2.9
    add_box(s7,x,y,5.8,2.5,CARD_BG,is_round=True);add_box(s7,x,y,5.8,0.05,ac)
    add_textbox(s7,x+0.3,y+0.25,5.2,0.5,ti,font_size=20,font_color=ac,bold=True,font_name="Trebuchet MS")
    add_textbox(s7,x+0.3,y+1.0,5.2,1.3,de,font_size=14,font_color=TEXT_COLOR)
for i in range(1, s7.Shapes.Count+1):
    add_anim(s7, s7.Shapes(i))

# ============ 幻灯片 8: 未来 ============
s8 = pres.Slides.Add(8, 12)
add_slide_bg(s8, LIGHT_BG); add_title_bar(s8, "Future Prospects"); add_page_num(s8, 8)
phases = [("Short-term","Improve multi-Agent\nOptimize RAG effect\nExpand template library",BLUE,1.0),
          ("Mid-term","Mobile app launch\nMore LLM integration\nOpen API ecosystem",TEAL,4.7),
          ("Long-term","Full-chain AI job platform\nFrom resume to onboarding\nIntelligent closed loop",ORANGE,8.4)]
for ti,de,ac,x in phases:
    add_box(s8,x,1.3,3.8,4.5,CARD_BG,is_round=True);add_box(s8,x,1.3,3.8,0.05,ac)
    add_textbox(s8,x+0.3,1.6,3.2,0.5,ti,font_size=22,font_color=ac,bold=True,font_name="Trebuchet MS",align=2)
    add_textbox(s8,x+0.3,2.4,3.2,3,de,font_size=16,font_color=TEXT_COLOR,align=2)
add_box(s8,1,6.4,11.333,0.5,BLUE)
add_textbox(s8,1,6.4,11.333,0.5,"Let everyone find their ideal job",font_size=16,font_color=WHITE,bold=True,font_name="Trebuchet MS",align=2)
for i in range(1, s8.Shapes.Count+1):
    add_anim(s8, s8.Shapes(i))

# ============ 幻灯片 9: 致谢 ============
s9 = pres.Slides.Add(9, 12)
add_slide_bg(s9, LIGHT_BG); add_page_num(s9, 9)
add_box(s9,5.5,2.0,2.333,0.04,BLUE)
add_textbox(s9,1,2.3,11.333,1,"Thank You",font_size=52,font_color=TEXT_COLOR,bold=True,font_name="Trebuchet MS",align=2)
add_textbox(s9,1,3.6,11.333,0.6,"EasyApplyResume -- AI-powered Smart Job Platform",font_size=18,font_color=BLUE,font_name="Trebuchet MS",align=2)
add_textbox(s9,1,5.0,11.333,0.4,"Spring Boot . Spring AI . MyBatis-Plus . Redis . PostgreSQL . React . Docker",font_size=12,font_color=GRAY,align=2)
add_textbox(s9,1,5.6,11.333,0.4,"shiningCloud2025 2026",font_size=11,font_color=GRAY,align=2)
for i in range(1, s9.Shapes.Count+1):
    add_anim(s9, s9.Shapes(i))


out_path = os.path.abspath("out/EasyApplyResume-项目介绍-COM.pptx")
pres.SaveAs(out_path)
pres.Close()
ppt.Quit()
print(f"Saved: {out_path}")
print(f"Slides: {pres.Slides.Count}")
