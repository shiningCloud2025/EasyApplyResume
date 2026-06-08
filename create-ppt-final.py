"""
完全用 python-pptx 生成 PPT，确保动画 XML 正确注入
"""
from pptx import Presentation
from pptx.util import Inches, Pt, Emu
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE
from lxml import etree
import copy
import uuid

# ============ XML 命名空间 ============
P_NS  = 'http://schemas.openxmlformats.org/presentationml/2006/main'
A_NS  = 'http://schemas.openxmlformats.org/drawingml/2006/main'
R_NS  = 'http://schemas.openxmlformats.org/officeDocument/2006/relationships'

# 注册命名空间
etree.register_namespace('a', 'http://schemas.openxmlformats.org/drawingml/2006/main')
etree.register_namespace('r', 'http://schemas.openxmlformats.org/officeDocument/2006/relationships')
etree.register_namespace('p', 'http://schemas.openxmlformats.org/presentationml/2006/main')

# ============ 颜色 ============
DARK   = RGBColor(0x1E, 0x3A, 0x5F)
BLUE   = RGBColor(0x0E, 0xA5, 0xE9)
TEAL   = RGBColor(0x14, 0xB8, 0xA6)
WHITE  = RGBColor(0xFF, 0xFF, 0xFF)
GRAY_BG = RGBColor(0xF1, 0xF5, 0xF9)
MID_GRAY = RGBColor(0x64, 0x74, 0x8B)
DARK_TEXT = RGBColor(0x1E, 0x29, 0x3B)
LIGHT_BLUE = RGBColor(0xE0, 0xF2, 0xFE)

# ============ 尺寸 ============
SLIDE_W = Inches(13.333)
SLIDE_H = Inches(7.5)

prs = Presentation()
prs.slide_width = SLIDE_W
prs.slide_height = SLIDE_H

# ============ 辅助函数 ============

def add_bg(slide, color):
    bg = slide.background
    fill = bg.fill
    fill.solid()
    fill.fore_color.rgb = color

def add_rect(slide, left, top, width, height, fill_color, line_color=None):
    shape = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, left, top, width, height)
    shape.fill.solid()
    shape.fill.fore_color.rgb = fill_color
    if line_color:
        shape.line.fill.solid()
        shape.line.fill.fore_color.rgb = line_color
    else:
        shape.line.fill.background()
    return shape

def add_rounded_rect(slide, left, top, width, height, fill_color):
    shape = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
    shape.fill.solid()
    shape.fill.fore_color.rgb = fill_color
    shape.line.fill.background()
    return shape

def add_text(slide, left, top, width, height, text, font_size=12, color=DARK_TEXT,
             bold=False, font_name='Calibri', align=PP_ALIGN.LEFT, v_align=MSO_ANCHOR.TOP):
    txBox = slide.shapes.add_textbox(left, top, width, height)
    tf = txBox.text_frame
    tf.word_wrap = True
    tf.auto_size = None
    p = tf.paragraphs[0]
    p.text = text
    p.font.size = Pt(font_size)
    p.font.color.rgb = color
    p.font.bold = bold
    p.font.name = font_name
    p.alignment = align
    return txBox

def add_multiline_text(slide, left, top, width, height, lines, font_name='Calibri'):
    """lines: [(text, font_size, color, bold, alignment), ...]"""
    txBox = slide.shapes.add_textbox(left, top, width, height)
    tf = txBox.text_frame
    tf.word_wrap = True
    tf.auto_size = None
    for i, (text, font_size, color, bold, alignment) in enumerate(lines):
        if i == 0:
            p = tf.paragraphs[0]
        else:
            p = tf.add_paragraph()
        p.text = text
        p.font.size = Pt(font_size)
        p.font.color.rgb = color
        p.font.bold = bold
        p.font.name = font_name
        p.alignment = alignment
    return txBox

def add_title_bar(slide, title):
    """添加统一的标题栏"""
    # 标题栏背景
    add_rect(slide, Inches(0), Inches(0), Inches(13.333), Inches(0.8), DARK)
    # 标题文字
    add_text(slide, Inches(0.6), Inches(0.15), Inches(12), Inches(0.5),
             title, font_size=20, color=WHITE, bold=True, font_name='Trebuchet MS',
             align=PP_ALIGN.LEFT, v_align=MSO_ANCHOR.MIDDLE)
    # 底部分割线
    add_rect(slide, Inches(0), Inches(0.8), Inches(13.333), Inches(0.03), BLUE)

def add_footer(slide, page_num):
    add_text(slide, Inches(11), Inches(7.1), Inches(2), Inches(0.3),
             f"EasyApplyResume | {page_num}", font_size=8, color=MID_GRAY,
             align=PP_ALIGN.RIGHT)

# ============ 动画注入 ============

def make_anim(shape_id, anim_type='fade', delay=0, duration=500):
    """
    创建点击触发的动画 XML
    anim_type: 'fade', 'zoom', 'fly_left', 'fly_right', 'wipe_down', 'float_up'
    """
    anim_id = str(uuid.uuid4())
    
    # 根节点: p:par
    par = etree.Element(f"{{{P_NS}}}par")
    
    # cTn (common time node)
    cTn = etree.SubElement(par, f"{{{P_NS}}}cTn")
    cTn.set("id", str(uuid.uuid4()))
    cTn.set("dur", str(duration))  # ms
    cTn.set("fill", "hold")
    
    stCondLst = etree.SubElement(cTn, f"{{{P_NS}}}stCondLst")
    cond = etree.SubElement(stCondLst, f"{{{P_NS}}}cond")
    cond.set("delay", str(delay))
    
    childTnLst = etree.SubElement(par, f"{{{P_NS}}}childTnLst")
    
    # 动画节点
    anim = etree.SubElement(childTnLst, f"{{{P_NS}}}anim")
    anim.set("calcmode", "lin")
    anim.set("valueType", "num")
    anim.set("to", "1")
    
    cBhvr = etree.SubElement(anim, f"{{{P_NS}}}cBhvr")
    cBhvr.set("accumulate", "none")
    cBhvr.set("additive", "base")
    
    cTn2 = etree.SubElement(cBhvr, f"{{{P_NS}}}cTn")
    cTn2.set("id", str(uuid.uuid4()))
    cTn2.set("dur", str(duration))
    cTn2.set("fill", "hold")
    
    stCondLst2 = etree.SubElement(cTn2, f"{{{P_NS}}}stCondLst")
    cond2 = etree.SubElement(stCondLst2, f"{{{P_NS}}}cond")
    cond2.set("delay", str(delay))
    
    tgtEl = etree.SubElement(cBhvr, f"{{{P_NS}}}tgtEl")
    spTgt = etree.SubElement(tgtEl, f"{{{P_NS}}}spTgt")
    spTgt.set("spid", str(shape_id))
    
    # 根据类型添加不同的动画子元素
    if anim_type == 'fade':
        anim.set("calcmode", "lin")
        # 简单的 alpha/fade 通过 set 实现
        cBhvr.set("rctx", "alpha")
    elif anim_type == 'zoom':
        anim.set("calcmode", "lin")
        cBhvr.set("rctx", "scale")
    elif anim_type == 'fly_left':
        anim.set("calcmode", "lin")
        cBhvr.set("rctx", "position")
        anim2 = etree.SubElement(childTnLst, f"{{{P_NS}}}anim")
        anim2.set("calcmode", "lin")
        anim2.set("valueType", "num")
        anim2.set("from", "-1")
        anim2.set("to", "0")
        cBhvr2 = etree.SubElement(anim2, f"{{{P_NS}}}cBhvr")
        cTn3 = etree.SubElement(cBhvr2, f"{{{P_NS}}}cTn")
        cTn3.set("id", str(uuid.uuid4()))
        cTn3.set("dur", str(duration))
        cTn3.set("fill", "hold")
        tgtEl2 = etree.SubElement(cBhvr2, f"{{{P_NS}}}tgtEl")
        spTgt2 = etree.SubElement(tgtEl2, f"{{{P_NS}}}spTgt")
        spTgt2.set("spid", str(shape_id))
    
    return par, anim_id

def inject_slide_anim(slide, shape_ids, anim_types=None, trans_type='fade'):
    """给幻灯片注入动画"""
    if anim_types is None:
        anim_types = ['fade'] * len(shape_ids)
    
    # 注入翻页过渡
    trans = etree.SubElement(slide.element, f"{{{P_NS}}}transition")
    trans.set("spd", "slow")
    if trans_type == 'fade':
        etree.SubElement(trans, f"{{{P_NS}}}fade")
    elif trans_type == 'push':
        push = etree.SubElement(trans, f"{{{P_NS}}}push")
        push.set("dir", "l")
    elif trans_type == 'cover':
        cover = etree.SubElement(trans, f"{{{P_NS}}}cover")
        cover.set("dir", "r")
    elif trans_type == 'wipe':
        wipe = etree.SubElement(trans, f"{{{P_NS}}}wipe")
        wipe.set("dir", "l")
    elif trans_type == 'uncover':
        uncover = etree.SubElement(trans, f"{{{P_NS}}}uncover")
        uncover.set("dir", "d")
    elif trans_type == 'dissolve':
        etree.SubElement(trans, f"{{{P_NS}}}dissolve")
    else:
        etree.SubElement(trans, f"{{{P_NS}}}fade")
    
    # 注入元素动画
    timing = etree.SubElement(slide.element, f"{{{P_NS}}}timing")
    tnLst = etree.SubElement(timing, f"{{{P_NS}}}tnLst")
    seq = etree.SubElement(tnLst, f"{{{P_NS}}}seq")
    seq.set("concurrent", "0")
    
    cTn_root = etree.SubElement(seq, f"{{{P_NS}}}cTn")
    cTn_root.set("id", str(uuid.uuid4()))
    cTn_root.set("dur", "indefinite")
    
    stCondLst_root = etree.SubElement(cTn_root, f"{{{P_NS}}}stCondLst")
    cond_root = etree.SubElement(stCondLst_root, f"{{{P_NS}}}cond")
    cond_root.set("delay", "0")
    
    childTnLst = etree.SubElement(seq, f"{{{P_NS}}}childTnLst")
    
    for i, (sid, at) in enumerate(zip(shape_ids, anim_types)):
        par, _ = make_anim(sid, at, delay=0, duration=500)
        childTnLst.append(par)

def get_shape_ids(slide):
    """获取幻灯片中所有形状的 ID"""
    ids = []
    for shape in slide.shapes:
        ids.append(shape.shape_id)
    return ids

# ======================================================================
#                              幻灯片内容
# ======================================================================

# ---------- 第1页：封面 ----------
s1 = prs.slides.add_slide(prs.slide_layouts[6])  # blank
add_bg(s1, DARK)

# 装饰线
add_rect(s1, Inches(1), Inches(2.2), Inches(0.08), Inches(2.8), BLUE)
# 项目名
add_text(s1, Inches(1.5), Inches(2.2), Inches(10), Inches(1.2),
         "EasyApplyResume", font_size=48, color=WHITE, bold=True,
         font_name='Trebuchet MS', align=PP_ALIGN.LEFT)
# 副标题
add_text(s1, Inches(1.5), Inches(3.5), Inches(10), Inches(0.8),
         "AI 驱动的智能求职与管理平台", font_size=24, color=BLUE,
         bold=False, font_name='Trebuchet MS', align=PP_ALIGN.LEFT)
# 分隔线
add_rect(s1, Inches(1.5), Inches(4.5), Inches(4), Inches(0.03), TEAL)
# 技术栈标签
add_text(s1, Inches(1.5), Inches(4.8), Inches(10), Inches(0.5),
         "Spring Boot 3.3 · Spring AI · MyBatis-Plus · Redis · PostgreSQL · Docker · React",
         font_size=14, color=MID_GRAY, font_name='Calibri', align=PP_ALIGN.LEFT)

s1_ids = get_shape_ids(s1)
inject_slide_anim(s1, s1_ids, 
    ['zoom', 'fade', 'fade', 'wipe_down', 'fade', 'fade'], 'fade')

# ---------- 第2页：问题背景 ----------
s2 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s2, GRAY_BG)
add_title_bar(s2, "🔍 问题背景")
add_footer(s2, 2)

problems = [
    ("简历制作效率低下", "用户在海量模板中难以选择\n手动排版耗时费力，格式不统一", BLUE),
    ("信息不对称", "求职者不了解企业真实需求\nJD与简历匹配度低", TEAL),
    ("运营管理负担重", "B端管理员需手动处理海量数据\n缺乏智能化运营工具", RGBColor(0xF5, 0x9E, 0x0B)),
    ("缺乏个性化服务", "通用模板无法满足不同行业需求\n缺少 AI 辅助优化建议", RGBColor(0xEF, 0x44, 0x44)),
]

for i, (title, desc, accent) in enumerate(problems):
    col = i % 2
    row = i // 2
    x = Inches(0.6 + col * 6.3)
    y = Inches(1.3 + row * 2.8)
    
    card = add_rect(s2, x, y, Inches(5.8), Inches(2.4), WHITE)
    # 左侧强调线
    add_rect(s2, x, y, Inches(0.06), Inches(2.4), accent)
    # 标题
    add_text(s2, x + Inches(0.3), y + Inches(0.2), Inches(5.2), Inches(0.4),
             title, font_size=16, color=DARK, bold=True, font_name='Trebuchet MS')
    # 描述
    add_text(s2, x + Inches(0.3), y + Inches(0.8), Inches(5.2), Inches(1.4),
             desc, font_size=13, color=MID_GRAY, font_name='Calibri')

s2_ids = get_shape_ids(s2)
inject_slide_anim(s2, s2_ids, ['fade'] * len(s2_ids), 'push')

# ---------- 第3页：技术选型 ----------
s3 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s3, GRAY_BG)
add_title_bar(s3, "🛠️ 技术选型")
add_footer(s3, 3)

tech_categories = [
    ("基础框架", [
        "Spring Boot 3.3.5 + Java 21",
        "Spring Security 双链认证",
        "MyBatis-Plus 3.5.7 双数据源",
        "Nacos 配置中心 & 服务发现",
    ], BLUE),
    ("AI & LLM", [
        "Spring AI 多模型适配",
        "ReAct Agent 智能体架构",
        "RAG 检索增强生成",
        "DashScope / 智谱 / Ollama",
    ], TEAL),
    ("数据与存储", [
        "MySQL + PostgreSQL/PgVector",
        "Redis 缓存 & 消息通知",
        "MinIO / 七牛云OSS",
        "Docker 容器化部署",
    ], RGBColor(0xF5, 0x9E, 0x0B)),
]

for i, (cat_name, items, accent) in enumerate(tech_categories):
    x = Inches(0.6 + i * 4.1)
    
    # 分类标题
    add_rect(s3, x, Inches(1.3), Inches(3.8), Inches(0.5), accent)
    add_text(s3, x + Inches(0.1), Inches(1.3), Inches(3.6), Inches(0.5),
             cat_name, font_size=15, color=WHITE, bold=True,
             font_name='Trebuchet MS', align=PP_ALIGN.CENTER, v_align=MSO_ANCHOR.MIDDLE)
    
    # 技术项
    for j, item in enumerate(items):
        item_y = Inches(2.1 + j * 0.55)
        card = add_rounded_rect(s3, x, item_y, Inches(3.8), Inches(0.45), WHITE)
        add_text(s3, x + Inches(0.15), item_y + Inches(0.08), Inches(3.5), Inches(0.3),
                 item, font_size=11, color=DARK_TEXT, font_name='Calibri',
                 v_align=MSO_ANCHOR.MIDDLE)

s3_ids = get_shape_ids(s3)
inject_slide_anim(s3, s3_ids, ['fade'] * len(s3_ids), 'cover')

# ---------- 第4页：AI智能体架构 ----------
s4 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s4, GRAY_BG)
add_title_bar(s4, "🧠 AI 智能体架构")
add_footer(s4, 4)

# 左侧：Agent 层级
agents = [
    ("BaseAgent", "LLM调用 · 记忆管理 · 基础抽象"),
    ("ReActAgent", "思考-行动-观察循环"),
    ("ToolCallAgent", "工具注册 · Function Calling"),
]
for i, (name, desc) in enumerate(agents):
    y = Inches(1.3 + i * 1.5)
    colors = [BLUE, TEAL, RGBColor(0xF5, 0x9E, 0x0B)]
    
    box = add_rounded_rect(s4, Inches(0.6), y, Inches(5.5), Inches(1.2), WHITE)
    add_rect(s4, Inches(0.6), y, Inches(0.06), Inches(1.2), colors[i])
    add_text(s4, Inches(0.9), y + Inches(0.15), Inches(5), Inches(0.4),
             name, font_size=18, color=colors[i], bold=True, font_name='Trebuchet MS')
    add_text(s4, Inches(0.9), y + Inches(0.6), Inches(5), Inches(0.5),
             desc, font_size=11, color=MID_GRAY, font_name='Calibri')
    # 连接箭头
    if i < 2:
        add_text(s4, Inches(3), y + Inches(1.2), Inches(1), Inches(0.3),
                 "▼", font_size=18, color=MID_GRAY, align=PP_ALIGN.CENTER)

# 右侧：核心组件
add_text(s4, Inches(6.8), Inches(1.3), Inches(6), Inches(0.5),
         "核心组件", font_size=16, color=DARK, bold=True, font_name='Trebuchet MS')

components = [
    ("RAG 知识库", "PgVector + 云知识库检索"),
    ("工具调用", "Web搜索·终端·文件·邮件·PDF"),
    ("对话记忆", "MySQL持久化 + 内存缓存"),
    ("安全过滤", "敏感词检测 · 权限控制"),
    ("MCP 客户端", "多协议工具集成"),
    ("流式输出", "SSE 实时对话响应"),
]
for i, (label, desc) in enumerate(components):
    y = Inches(1.9 + i * 0.82)
    card = add_rounded_rect(s4, Inches(6.8), y, Inches(6), Inches(0.7), WHITE)
    add_rect(s4, Inches(6.8), y, Inches(0.06), Inches(0.7),
             BLUE if i % 2 == 0 else TEAL)
    add_text(s4, Inches(7.1), y + Inches(0.05), Inches(5.4), Inches(0.3),
             label, font_size=13, color=DARK, bold=True, font_name='Trebuchet MS')
    add_text(s4, Inches(7.1), y + Inches(0.35), Inches(5.4), Inches(0.3),
             desc, font_size=10, color=MID_GRAY, font_name='Calibri')

s4_ids = get_shape_ids(s4)
inject_slide_anim(s4, s4_ids, ['fade'] * len(s4_ids), 'wipe')

# ---------- 第5页：C端功能 ----------
s5 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s5, GRAY_BG)
add_title_bar(s5, "📝 C端 — 简历AI助手")
add_footer(s5, 5)

features_c = [
    ("智能简历润色", "AI分析简历内容\n自动优化措辞与排版", BLUE),
    ("定制化建议", "根据目标JD提供\n针对性修改建议", TEAL),
    ("RAG增强检索", "向量数据库知识库\n精准匹配行业需求", RGBColor(0xF5, 0x9E, 0x0B)),
    ("工具调用能力", "Web搜索·PDF生成\n邮件发送·文件操作", RGBColor(0x8B, 0x5C, 0xF6)),
    ("流式对话体验", "SSE实时响应\n打字机效果呈现", RGBColor(0x10, 0xB9, 0x81)),
    ("生态服务集成", "MinIO/七牛云存储\n多行业模板支持", RGBColor(0xEF, 0x44, 0x44)),
]

for i, (title, desc, accent) in enumerate(features_c):
    col = i % 3
    row = i // 3
    x = Inches(0.6 + col * 4.15)
    y = Inches(1.3 + row * 2.9)
    
    card = add_rounded_rect(s5, x, y, Inches(3.8), Inches(2.5), WHITE)
    # 顶部强调条
    add_rect(s5, x, y, Inches(3.8), Inches(0.06), accent)
    add_text(s5, x + Inches(0.2), y + Inches(0.3), Inches(3.4), Inches(0.5),
             title, font_size=15, color=DARK, bold=True, font_name='Trebuchet MS')
    add_text(s5, x + Inches(0.2), y + Inches(1.0), Inches(3.4), Inches(1.3),
             desc, font_size=12, color=MID_GRAY, font_name='Calibri')

s5_ids = get_shape_ids(s5)
inject_slide_anim(s5, s5_ids, ['fade'] * len(s5_ids), 'uncover')

# ---------- 第6页：B端功能 ----------
s6 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s6, GRAY_BG)
add_title_bar(s6, "⚙️ B端 — 系统管理助手")
add_footer(s6, 6)

admin_modules = [
    ("用户管理", "用户注册/登录/信息管理\n权限控制与角色分配"),
    ("简历模板管理", "行业模板CRUD\n模板启用/禁用控制"),
    ("题库管理", "笔试题目增删改查\n分类管理与难度设置"),
    ("FAQ管理", "常见问题维护\n内容审核与发布"),
    ("使用指南管理", "用户手册内容管理\n图文编辑与发布"),
]

for i, (title, desc) in enumerate(admin_modules):
    y = Inches(1.3 + i * 1.15)
    card = add_rounded_rect(s6, Inches(0.6), y, Inches(6.5), Inches(1.0), WHITE)
    add_rect(s6, Inches(0.6), y, Inches(0.06), Inches(1.0), BLUE)
    add_text(s6, Inches(0.9), y + Inches(0.1), Inches(6), Inches(0.3),
             title, font_size=15, color=DARK, bold=True, font_name='Trebuchet MS')
    add_text(s6, Inches(0.9), y + Inches(0.45), Inches(6), Inches(0.5),
             desc, font_size=11, color=MID_GRAY, font_name='Calibri')

# 右侧 AI 能力
add_text(s6, Inches(7.6), Inches(1.3), Inches(5.5), Inches(0.5),
         "AI 管理能力", font_size=16, color=DARK, bold=True, font_name='Trebuchet MS')

ai_admin = [
    "智能数据统计与分析",
    "自动化运营建议",
    "异常行为检测预警",
    "智能问答辅助",
    "内容自动审核",
    "批量操作自动化",
    "日志分析与诊断",
]
for i, feat in enumerate(ai_admin):
    y = Inches(1.9 + i * 0.45)
    add_text(s6, Inches(7.6), y, Inches(5.5), Inches(0.4),
             f"✦ {feat}", font_size=12, color=DARK_TEXT, font_name='Calibri')

s6_ids = get_shape_ids(s6)
inject_slide_anim(s6, s6_ids, ['fade'] * len(s6_ids), 'fade')

# ---------- 第7页：D端功能 ----------
s7 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s7, GRAY_BG)
add_title_bar(s7, "📊 D端 — 数据与监控平台")
add_footer(s7, 7)

d_features = [
    ("实时访问监控", "日活/总访问量追踪\nC端/B端多维度数据看板", BLUE),
    ("广告投放管理", "广告上下架与排期控制\n用户端展示与效果追踪", TEAL),
    ("服务机器监控", "SSH远程连接管理\n服务器健康检测与告警", RGBColor(0xF5, 0x9E, 0x0B)),
    ("智能数据报表", "Prometheus指标采集\nGrafana大屏可视化", RGBColor(0x8B, 0x5C, 0xF6)),
]

for i, (title, desc, accent) in enumerate(d_features):
    col = i % 2
    row = i // 2
    x = Inches(0.6 + col * 6.3)
    y = Inches(1.3 + row * 2.95)
    
    card = add_rounded_rect(s7, x, y, Inches(5.8), Inches(2.5), WHITE)
    add_rect(s7, x, y, Inches(5.8), Inches(0.06), accent)
    add_text(s7, x + Inches(0.3), y + Inches(0.3), Inches(5.2), Inches(0.5),
             title, font_size=18, color=DARK, bold=True, font_name='Trebuchet MS')
    add_text(s7, x + Inches(0.3), y + Inches(1.0), Inches(5.2), Inches(1.3),
             desc, font_size=14, color=MID_GRAY, font_name='Calibri')

s7_ids = get_shape_ids(s7)
inject_slide_anim(s7, s7_ids, ['fade'] * len(s7_ids), 'push')

# ---------- 第8页：未来前景 ----------
s8 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s8, GRAY_BG)
add_title_bar(s8, "🚀 未来前景")
add_footer(s8, 8)

phases = [
    ("近期目标", "完善多Agent协作 · 优化RAG效果\n扩展行业模板库 · 增强C端体验", BLUE, Inches(1.0)),
    ("中期规划", "上线移动端 · 接入更多LLM\n开放API生态 · 企业级SaaS", TEAL, Inches(4.7)),
    ("远期愿景", "打造全链路AI求职平台\n从简历到入职的智能闭环", RGBColor(0xF5, 0x9E, 0x0B), Inches(8.4)),
]

for title, desc, accent, x in phases:
    card = add_rounded_rect(s8, x, Inches(1.5), Inches(3.8), Inches(4.5), WHITE)
    add_rect(s8, x, Inches(1.5), Inches(3.8), Inches(0.06), accent)
    add_text(s8, x + Inches(0.3), Inches(1.8), Inches(3.2), Inches(0.5),
             title, font_size=20, color=accent, bold=True, font_name='Trebuchet MS',
             align=PP_ALIGN.CENTER)
    add_text(s8, x + Inches(0.3), Inches(2.6), Inches(3.2), Inches(3),
             desc, font_size=14, color=MID_GRAY, font_name='Calibri', align=PP_ALIGN.CENTER)

# 底部愿景
add_rect(s8, Inches(1), Inches(6.5), Inches(11.333), Inches(0.5), DARK)
add_text(s8, Inches(1), Inches(6.5), Inches(11.333), Inches(0.5),
         "让每个人都能找到理想的工作 — AI 赋能求职全流程",
         font_size=16, color=WHITE, bold=True, font_name='Trebuchet MS',
         align=PP_ALIGN.CENTER, v_align=MSO_ANCHOR.MIDDLE)

s8_ids = get_shape_ids(s8)
inject_slide_anim(s8, s8_ids, ['fade'] * len(s8_ids), 'cover')

# ---------- 第9页：致谢 ----------
s9 = prs.slides.add_slide(prs.slide_layouts[6])
add_bg(s9, DARK)

# 装饰线
add_rect(s9, Inches(5.5), Inches(2.2), Inches(2.333), Inches(0.05), BLUE)

add_text(s9, Inches(1), Inches(2.5), Inches(11.333), Inches(1.2),
         "感谢聆听", font_size=48, color=WHITE, bold=True,
         font_name='Trebuchet MS', align=PP_ALIGN.CENTER)

add_text(s9, Inches(1), Inches(3.8), Inches(11.333), Inches(0.6),
         "EasyApplyResume — AI 驱动的智能求职与管理平台",
         font_size=18, color=BLUE, font_name='Trebuchet MS', align=PP_ALIGN.CENTER)

add_text(s9, Inches(1), Inches(5.0), Inches(11.333), Inches(0.5),
         "Spring Boot · Spring AI · MyBatis-Plus · Redis · PostgreSQL · Docker · React",
         font_size=13, color=MID_GRAY, font_name='Calibri', align=PP_ALIGN.CENTER)

add_text(s9, Inches(1), Inches(5.6), Inches(11.333), Inches(0.5),
         "shiningCloud2025 © 2026",
         font_size=11, color=MID_GRAY, font_name='Calibri', align=PP_ALIGN.CENTER)

s9_ids = get_shape_ids(s9)
inject_slide_anim(s9, s9_ids, ['fade'] * len(s9_ids), 'dissolve')

# ============ 保存 ============
output_path = "out/EasyApplyResume-项目介绍-v5.pptx"
prs.save(output_path)
print(f"[OK] Saved: {output_path}")

# 验证
prs2 = Presentation(output_path)
for i, s in enumerate(prs2.slides):
    tm = s.element.find(f'{{{P_NS}}}timing')
    tr = s.element.find(f'{{{P_NS}}}transition')
    if tm is not None:
        pars = tm.findall(f'.//{{{P_NS}}}par')
        print(f"  Slide {i+1}: transition={tr is not None}, anims={len(pars)}")
    else:
        print(f"  Slide {i+1}: NO TIMING!")
print(f"\n[DONE] 共 {len(prs2.slides)} 页")
