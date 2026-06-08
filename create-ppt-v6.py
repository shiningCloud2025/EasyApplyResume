"""
全新 PPT 生成：全页统一深色风格 + 正确的点击触发动画
"""
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE
from lxml import etree
import uuid

P_NS = 'http://schemas.openxmlformats.org/presentationml/2006/main'
A_NS = 'http://schemas.openxmlformats.org/drawingml/2006/main'

etree.register_namespace('a', A_NS)
etree.register_namespace('r', 'http://schemas.openxmlformats.org/officeDocument/2006/relationships')
etree.register_namespace('p', P_NS)

# ============ 深色统一配色 ============
BG_DARK   = RGBColor(0x0A, 0x0F, 0x1A)      # 接近纯黑深蓝底
CARD_BG   = RGBColor(0x14, 0x1F, 0x33)      # 卡片背景
BLUE      = RGBColor(0x38, 0xBD, 0xF8)      # 亮蓝
TEAL      = RGBColor(0x2D, 0xD4, 0xBF)      # 青色
ORANGE    = RGBColor(0xFB, 0x92, 0x3C)      # 橙色
PURPLE    = RGBColor(0xA7, 0x8B, 0xFA)      # 紫色
RED       = RGBColor(0xF8, 0x71, 0x71)      # 红色
WHITE     = RGBColor(0xFF, 0xFF, 0xFF)
GRAY      = RGBColor(0x94, 0xA3, 0xB8)      # 灰色文字
DARK_GRAY = RGBColor(0x64, 0x74, 0x8B)      # 暗灰
LIGHT_BLUE_BG = RGBColor(0x0C, 0x1B, 0x33)  # 略亮的蓝底（用于代码区）

prs = Presentation()
prs.slide_width = Inches(13.333)
prs.slide_height = Inches(7.5)
SLIDE_W = prs.slide_width
SLIDE_H = prs.slide_height

# ============ 辅助函数 ============
def set_bg(slide, color):
    slide.background.fill.solid()
    slide.background.fill.fore_color.rgb = color

def add_rect(slide, l, t, w, h, fill, line=None):
    s = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, l, t, w, h)
    s.fill.solid()
    s.fill.fore_color.rgb = fill
    if line: s.line.fill.solid(); s.line.fill.fore_color.rgb = line
    else: s.line.fill.background()
    return s

def add_rrect(slide, l, t, w, h, fill):
    s = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, l, t, w, h)
    s.fill.solid()
    s.fill.fore_color.rgb = fill
    s.line.fill.background()
    return s

def add_text(slide, l, t, w, h, text, size=12, color=WHITE, bold=False,
             font='Calibri', align=PP_ALIGN.LEFT, va=MSO_ANCHOR.TOP):
    tb = slide.shapes.add_textbox(l, t, w, h)
    tf = tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = text
    p.font.size = Pt(size)
    p.font.color.rgb = color
    p.font.bold = bold
    p.font.name = font
    p.alignment = align
    return tb

def add_title_bar(slide, icon_text):
    """统一顶部标题栏"""
    add_rect(slide, Inches(0), Inches(0), SLIDE_W, Inches(0.75), RGBColor(0x0E, 0x18, 0x2A))
    add_text(slide, Inches(0.6), Inches(0.12), Inches(12), Inches(0.5),
             icon_text, size=22, color=BLUE, bold=True, font='Trebuchet MS', va=MSO_ANCHOR.MIDDLE)
    add_rect(slide, Inches(0), Inches(0.75), SLIDE_W, Inches(0.02), BLUE)

def add_page_num(slide, n):
    add_text(slide, Inches(11.5), Inches(7.1), Inches(1.5), Inches(0.3),
             f"{n}/9", size=9, color=DARK_GRAY, align=PP_ALIGN.RIGHT)

# ============ 动画注入（正确版） ============
def make_onclick_anim(shape_id, effect='fade', duration=400):
    """
    创建点击触发淡入动画
    """
    par = etree.Element(f"{{{P_NS}}}par")
    
    # 顶层 cTn - 控制触发条件
    cTn = etree.SubElement(par, f"{{{P_NS}}}cTn")
    cTn.set("id", str(uuid.uuid4()))
    cTn.set("dur", str(duration))
    cTn.set("fill", "hold")
    
    stCondLst = etree.SubElement(cTn, f"{{{P_NS}}}stCondLst")
    cond = etree.SubElement(stCondLst, f"{{{P_NS}}}cond")
    cond.set("delay", "0")
    # 这是关键：设置点击触发
    cond.set(f"evt", f"onclick")
    
    # 子动画列表
    childTnLst = etree.SubElement(par, f"{{{P_NS}}}childTnLst")
    
    if effect == 'fade':
        # 淡入 = alpha 从 0 到 1
        anim = etree.SubElement(childTnLst, f"{{{P_NS}}}anim")
        anim.set("calcmode", "lin")
        anim.set("valueType", "num")
        anim.set("to", "1")
        
        cBhvr = etree.SubElement(anim, f"{{{P_NS}}}cBhvr")
        cBhvr.set("accumulate", "none")
        cBhvr.set("additive", "base")
        cBhvr.set("rctx", "alpha")
        
        cbTn = etree.SubElement(cBhvr, f"{{{P_NS}}}cTn")
        cbTn.set("id", str(uuid.uuid4()))
        cbTn.set("dur", str(duration))
        cbTn.set("fill", "hold")
        
        tgtEl = etree.SubElement(cBhvr, f"{{{P_NS}}}tgtEl")
        spTgt = etree.SubElement(tgtEl, f"{{{P_NS}}}spTgt")
        spTgt.set("spid", str(shape_id))
    
    elif effect == 'fly_left':
        # 从左侧飞入
        anim = etree.SubElement(childTnLst, f"{{{P_NS}}}anim")
        anim.set("calcmode", "lin")
        anim.set("valueType", "num")
        anim.set("from", "-0.5")
        anim.set("to", "0")
        
        cBhvr = etree.SubElement(anim, f"{{{P_NS}}}cBhvr")
        cBhvr.set("accumulate", "none")
        cBhvr.set("additive", "base")
        cBhvr.set("rctx", "position")
        
        cbTn = etree.SubElement(cBhvr, f"{{{P_NS}}}cTn")
        cbTn.set("id", str(uuid.uuid4()))
        cbTn.set("dur", str(duration))
        cbTn.set("fill", "hold")
        
        tgtEl = etree.SubElement(cBhvr, f"{{{P_NS}}}tgtEl")
        spTgt = etree.SubElement(tgtEl, f"{{{P_NS}}}spTgt")
        spTgt.set("spid", str(shape_id))
    
    elif effect == 'zoom':
        # 缩放进入
        anim = etree.SubElement(childTnLst, f"{{{P_NS}}}anim")
        anim.set("calcmode", "lin")
        anim.set("valueType", "num")
        anim.set("by", "100")
        
        cBhvr = etree.SubElement(anim, f"{{{P_NS}}}cBhvr")
        cBhvr.set("accumulate", "none")
        cBhvr.set("additive", "base")
        cBhvr.set("rctx", "scale")
        
        cbTn = etree.SubElement(cBhvr, f"{{{P_NS}}}cTn")
        cbTn.set("id", str(uuid.uuid4()))
        cbTn.set("dur", str(duration))
        cbTn.set("fill", "hold")
        
        tgtEl = etree.SubElement(cBhvr, f"{{{P_NS}}}tgtEl")
        spTgt = etree.SubElement(tgtEl, f"{{{P_NS}}}spTgt")
        spTgt.set("spid", str(shape_id))
    
    return par

def add_animations(slide, shape_ids, trans='fade'):
    """
    给幻灯片加翻页过渡 + 所有形状的点击淡入动画
    shape_ids: 每个形状一个动画效果
    格式: [(shape_id, 'fade'), (shape_id, 'fly_left'), ...]
    """
    # 翻页过渡
    trans_el = etree.Element(f"{{{P_NS}}}transition")
    trans_el.set("spd", "med")
    if trans == 'push':
        p = etree.SubElement(trans_el, f"{{{P_NS}}}push")
        p.set("dir", "l")
    elif trans == 'cover':
        c = etree.SubElement(trans_el, f"{{{P_NS}}}cover")
        c.set("dir", "r")
    elif trans == 'wipe':
        w = etree.SubElement(trans_el, f"{{{P_NS}}}wipe")
        w.set("dir", "l")
    elif trans == 'uncover':
        u = etree.SubElement(trans_el, f"{{{P_NS}}}uncover")
        u.set("dir", "d")
    elif trans == 'dissolve':
        etree.SubElement(trans_el, f"{{{P_NS}}}dissolve")
    else:
        etree.SubElement(trans_el, f"{{{P_NS}}}fade")
    slide.element.append(trans_el)
    
    # 元素动画序列
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
    
    for sid, effect in shape_ids:
        par = make_onclick_anim(sid, effect)
        childTnLst.append(par)
    
    slide.element.append(timing)


# ======================================================================
# 幻灯片 1：封面
# ======================================================================
s1 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s1, BG_DARK)

# 装饰元素
s1_shapes = []
add_rect(s1, Inches(1), Inches(2.0), Inches(0.06), Inches(2.5), BLUE)
tb = add_text(s1, Inches(1.5), Inches(2.0), Inches(10), Inches(1.0),
         "EasyApplyResume", size=52, color=WHITE, bold=True, font='Trebuchet MS')
s1_shapes.append(tb.shape_id)
tb2 = add_text(s1, Inches(1.5), Inches(3.2), Inches(10), Inches(0.7),
         "AI 驱动的智能求职与管理平台", size=24, color=BLUE, font='Trebuchet MS')
s1_shapes.append(tb2.shape_id)
tb3 = add_text(s1, Inches(1.5), Inches(4.6), Inches(10), Inches(0.5),
         "Spring Boot · Spring AI · MyBatis-Plus · Redis · PostgreSQL · React · Docker",
         size=13, color=GRAY)
s1_shapes.append(tb3.shape_id)
add_rect(s1, Inches(1.5), Inches(4.2), Inches(5), Inches(0.02), TEAL)

# 动画
add_animations(s1, [(s1_shapes[0], 'zoom'), (s1_shapes[1], 'fade'), (s1_shapes[2], 'fade'), (2, 'fade'), (4, 'fade')],
               trans='fade')


# ======================================================================
# 幻灯片 2：问题背景
# ======================================================================
s2 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s2, BG_DARK)
add_title_bar(s2, "🔍 问题背景")
add_page_num(s2, 2)

problems = [
    ("简历制作低效", "海量模板难选择\n手动排版耗时长", BLUE),
    ("信息不对称", "不了解企业真实需求\nJD 与简历匹配度低", TEAL),
    ("运营负担重", "海量数据需手动管理\n缺乏智能化运营工具", ORANGE),
    ("缺乏个性化", "通用模板不匹配行业\n缺少 AI 辅助优化", PURPLE),
]

s2_ids = []
for i, (title, desc, accent) in enumerate(problems):
    x = Inches(0.6 + (i % 2) * 6.3)
    y = Inches(1.2 + (i // 2) * 2.85)
    card = add_rrect(s2, x, y, Inches(5.8), Inches(2.4), CARD_BG)
    s2_ids.append((card.shape_id, 'fade'))
    add_rect(s2, x, y, Inches(0.06), Inches(2.4), accent)
    s2_ids.append((s2.shapes[-1].shape_id, 'fade'))
    t = add_text(s2, x + Inches(0.3), y + Inches(0.2), Inches(5.2), Inches(0.45),
                 title, size=18, color=accent, bold=True, font='Trebuchet MS')
    s2_ids.append((t.shape_id, 'fade'))
    d = add_text(s2, x + Inches(0.3), y + Inches(0.85), Inches(5.2), Inches(1.3),
                 desc, size=14, color=GRAY)
    s2_ids.append((d.shape_id, 'fade'))

add_animations(s2, s2_ids, trans='push')


# ======================================================================
# 幻灯片 3：技术选型
# ======================================================================
s3 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s3, BG_DARK)
add_title_bar(s3, "🛠️ 技术选型")
add_page_num(s3, 3)

categories = [
    ("基础框架", ["Spring Boot 3.3.5 + Java 21", "Spring Security 双链认证",
                   "MyBatis-Plus 3.5.7", "Nacos 配置中心"], BLUE),
    ("AI & LLM", ["Spring AI 多模型适配", "ReAct Agent 智能体",
                   "RAG 检索增强生成", "DashScope / 智谱 / Ollama"], TEAL),
    ("数据与存储", ["MySQL + PostgreSQL/PgVector", "Redis 缓存 & 消息",
                    "MinIO / 七牛云OSS", "Docker 容器化部署"], ORANGE),
]

s3_ids = []
for ci, (cat, items, accent) in enumerate(categories):
    x = Inches(0.6 + ci * 4.1)
    hdr = add_rect(s3, x, Inches(1.2), Inches(3.8), Inches(0.5), accent)
    s3_ids.append((hdr.shape_id, 'fly_left'))
    ht = add_text(s3, x + Inches(0.1), Inches(1.2), Inches(3.6), Inches(0.5),
                  cat, size=15, color=BG_DARK, bold=True, font='Trebuchet MS',
                  align=PP_ALIGN.CENTER, va=MSO_ANCHOR.MIDDLE)
    s3_ids.append((ht.shape_id, 'fade'))
    
    for j, item in enumerate(items):
        iy = Inches(2.0 + j * 0.55)
        ic = add_rrect(s3, x, iy, Inches(3.8), Inches(0.45), CARD_BG)
        s3_ids.append((ic.shape_id, 'fade'))
        it = add_text(s3, x + Inches(0.15), iy + Inches(0.08), Inches(3.5), Inches(0.3),
                      item, size=11, color=GRAY, va=MSO_ANCHOR.MIDDLE)
        s3_ids.append((it.shape_id, 'fade'))

add_animations(s3, s3_ids, trans='cover')


# ======================================================================
# 幻灯片 4：AI 智能体架构
# ======================================================================
s4 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s4, BG_DARK)
add_title_bar(s4, "🧠 AI 智能体架构")
add_page_num(s4, 4)

agents = [
    ("BaseAgent", "LLM调用 · 记忆管理 · 基础抽象", BLUE),
    ("ReActAgent", "思考 → 行动 → 观察循环", TEAL),
    ("ToolCallAgent", "工具注册 · Function Calling", ORANGE),
]

s4_ids = []
for i, (name, desc, accent) in enumerate(agents):
    y = Inches(1.2 + i * 1.5)
    box = add_rrect(s4, Inches(0.6), y, Inches(5.5), Inches(1.2), CARD_BG)
    s4_ids.append((box.shape_id, 'fade'))
    add_rect(s4, Inches(0.6), y, Inches(0.06), Inches(1.2), accent)
    s4_ids.append((s4.shapes[-1].shape_id, 'fade'))
    nt = add_text(s4, Inches(1.0), y + Inches(0.15), Inches(5), Inches(0.4),
                  name, size=20, color=accent, bold=True, font='Trebuchet MS')
    s4_ids.append((nt.shape_id, 'fade'))
    dt = add_text(s4, Inches(1.0), y + Inches(0.6), Inches(5), Inches(0.5),
                  desc, size=12, color=GRAY)
    s4_ids.append((dt.shape_id, 'fade'))
    if i < 2:
        ar = add_text(s4, Inches(3), y + Inches(1.15), Inches(1), Inches(0.3),
                      "⬇", size=16, color=GRAY, align=PP_ALIGN.CENTER)
        s4_ids.append((ar.shape_id, 'fade'))

# 右侧组件列表
ct = add_text(s4, Inches(6.8), Inches(1.2), Inches(6), Inches(0.5),
              "核心组件", size=18, color=WHITE, bold=True, font='Trebuchet MS')
s4_ids.append((ct.shape_id, 'fade'))

comps = [("RAG 知识库", "PgVector + 云知识库", BLUE),
         ("工具调用", "Web搜索 · 终端 · 文件 · PDF", TEAL),
         ("对话记忆", "MySQL 持久化 + 内存", ORANGE),
         ("安全过滤", "敏感词 · 权限控制", PURPLE),
         ("MCP 客户端", "多协议工具集成", BLUE),
         ("流式输出", "SSE 实时对话", TEAL)]

for i, (label, desc, accent) in enumerate(comps):
    y = Inches(1.85 + i * 0.82)
    box = add_rrect(s4, Inches(6.8), y, Inches(6), Inches(0.7), CARD_BG)
    s4_ids.append((box.shape_id, 'fade'))
    add_rect(s4, Inches(6.8), y, Inches(0.06), Inches(0.7), accent)
    s4_ids.append((s4.shapes[-1].shape_id, 'fade'))
    lt = add_text(s4, Inches(7.1), y + Inches(0.05), Inches(5.4), Inches(0.3),
                  label, size=14, color=accent, bold=True, font='Trebuchet MS')
    s4_ids.append((lt.shape_id, 'fade'))
    ld = add_text(s4, Inches(7.1), y + Inches(0.38), Inches(5.4), Inches(0.3),
                  desc, size=11, color=GRAY)
    s4_ids.append((ld.shape_id, 'fade'))

add_animations(s4, s4_ids, trans='wipe')


# ======================================================================
# 幻灯片 5：C端功能
# ======================================================================
s5 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s5, BG_DARK)
add_title_bar(s5, "📝 C端 — 简历AI助手")
add_page_num(s5, 5)

feats = [
    ("智能简历润色", "AI 分析并优化简历\n自动调整措辞与排版", BLUE),
    ("定制化建议", "根据目标 JD 提供\n针对性修改方案", TEAL),
    ("RAG 增强检索", "向量数据库匹配\n精准推荐行业知识", ORANGE),
    ("工具调用", "Web搜索 · PDF生成\n邮件发送 · 文件操作", PURPLE),
    ("流式对话", "SSE 实时响应\n打字机效果呈现", BLUE),
    ("生态服务", "MinIO / 七牛云存储\n多行业模板支持", TEAL),
]

s5_ids = []
for i, (title, desc, accent) in enumerate(feats):
    x = Inches(0.6 + (i % 3) * 4.15)
    y = Inches(1.2 + (i // 3) * 2.85)
    card = add_rrect(s5, x, y, Inches(3.8), Inches(2.45), CARD_BG)
    s5_ids.append((card.shape_id, 'fade'))
    add_rect(s5, x, y, Inches(3.8), Inches(0.05), accent)
    s5_ids.append((s5.shapes[-1].shape_id, 'fade'))
    ft = add_text(s5, x + Inches(0.2), y + Inches(0.25), Inches(3.4), Inches(0.45),
                  title, size=16, color=accent, bold=True, font='Trebuchet MS')
    s5_ids.append((ft.shape_id, 'fade'))
    fd = add_text(s5, x + Inches(0.2), y + Inches(0.9), Inches(3.4), Inches(1.3),
                  desc, size=12, color=GRAY)
    s5_ids.append((fd.shape_id, 'fade'))

add_animations(s5, s5_ids, trans='uncover')


# ======================================================================
# 幻灯片 6：B端功能
# ======================================================================
s6 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s6, BG_DARK)
add_title_bar(s6, "⚙️ B端 — 系统管理助手")
add_page_num(s6, 6)

mods = [
    ("用户管理", "注册/登录 · 权限分配 · 角色控制"),
    ("简历模板", "行业模板 CRUD · 启用/禁用"),
    ("题库管理", "题目增删改查 · 分类与难度"),
    ("FAQ管理", "常见问题维护 · 内容审核"),
    ("使用指南", "手册编辑 · 图文发布"),
]

s6_ids = []
for i, (title, desc) in enumerate(mods):
    y = Inches(1.15 + i * 1.12)
    card = add_rrect(s6, Inches(0.6), y, Inches(6.5), Inches(0.95), CARD_BG)
    s6_ids.append((card.shape_id, 'fly_left'))
    add_rect(s6, Inches(0.6), y, Inches(0.06), Inches(0.95), BLUE)
    s6_ids.append((s6.shapes[-1].shape_id, 'fade'))
    mt = add_text(s6, Inches(1.0), y + Inches(0.1), Inches(5.8), Inches(0.3),
                  title, size=16, color=BLUE, bold=True, font='Trebuchet MS')
    s6_ids.append((mt.shape_id, 'fade'))
    md = add_text(s6, Inches(1.0), y + Inches(0.45), Inches(5.8), Inches(0.4),
                  desc, size=11, color=GRAY)
    s6_ids.append((md.shape_id, 'fade'))

# 右侧 AI 能力
ait = add_text(s6, Inches(7.6), Inches(1.15), Inches(5.5), Inches(0.45),
               "AI 管理能力", size=18, color=TEAL, bold=True, font='Trebuchet MS')
s6_ids.append((ait.shape_id, 'zoom'))

ai_items = ["智能数据统计与分析", "自动化运营建议", "异常行为检测预警",
            "智能问答辅助", "内容自动审核", "批量操作自动化", "日志诊断分析"]
for i, item in enumerate(ai_items):
    y = Inches(1.8 + i * 0.5)
    it = add_text(s6, Inches(7.6), y, Inches(5.5), Inches(0.4),
                  f"✦ {item}", size=13, color=GRAY)
    s6_ids.append((it.shape_id, 'fade'))

add_animations(s6, s6_ids, trans='fade')


# ======================================================================
# 幻灯片 7：D端功能
# ======================================================================
s7 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s7, BG_DARK)
add_title_bar(s7, "📊 D端 — 数据与监控平台")
add_page_num(s7, 7)

dfs = [
    ("实时访问监控", "日活/总访问量追踪\nC端/B端多维数据看板", BLUE),
    ("广告投放管理", "广告上下架与排期\n用户端展示效果追踪", TEAL),
    ("服务机器监控", "SSH 远程连接管理\n健康检测与实时告警", ORANGE),
    ("智能数据报表", "Prometheus 指标采集\nGrafana 大屏可视化", PURPLE),
]

s7_ids = []
for i, (title, desc, accent) in enumerate(dfs):
    x = Inches(0.6 + (i % 2) * 6.3)
    y = Inches(1.2 + (i // 2) * 2.9)
    card = add_rrect(s7, x, y, Inches(5.8), Inches(2.5), CARD_BG)
    s7_ids.append((card.shape_id, 'zoom'))
    add_rect(s7, x, y, Inches(5.8), Inches(0.05), accent)
    s7_ids.append((s7.shapes[-1].shape_id, 'fade'))
    dt = add_text(s7, x + Inches(0.3), y + Inches(0.25), Inches(5.2), Inches(0.5),
                  title, size=20, color=accent, bold=True, font='Trebuchet MS')
    s7_ids.append((dt.shape_id, 'fade'))
    dd = add_text(s7, x + Inches(0.3), y + Inches(1.0), Inches(5.2), Inches(1.3),
                  desc, size=14, color=GRAY)
    s7_ids.append((dd.shape_id, 'fade'))

add_animations(s7, s7_ids, trans='push')


# ======================================================================
# 幻灯片 8：未来前景
# ======================================================================
s8 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s8, BG_DARK)
add_title_bar(s8, "🚀 未来前景")
add_page_num(s8, 8)

phases = [
    ("近期目标", "完善多Agent协作\n优化RAG效果\n扩展行业模板库", BLUE, Inches(1.0)),
    ("中期规划", "上线移动端\n接入更多LLM\n开放API生态", TEAL, Inches(4.7)),
    ("远期愿景", "全链路AI求职平台\n从简历到入职\n智能闭环", ORANGE, Inches(8.4)),
]

s8_ids = []
for title, desc, accent, x in phases:
    card = add_rrect(s8, x, Inches(1.3), Inches(3.8), Inches(4.5), CARD_BG)
    s8_ids.append((card.shape_id, 'zoom'))
    add_rect(s8, x, Inches(1.3), Inches(3.8), Inches(0.05), accent)
    s8_ids.append((s8.shapes[-1].shape_id, 'fade'))
    pt = add_text(s8, x + Inches(0.3), Inches(1.6), Inches(3.2), Inches(0.5),
                  title, size=22, color=accent, bold=True, font='Trebuchet MS',
                  align=PP_ALIGN.CENTER)
    s8_ids.append((pt.shape_id, 'fade'))
    pd = add_text(s8, x + Inches(0.3), Inches(2.4), Inches(3.2), Inches(3),
                  desc, size=16, color=GRAY, align=PP_ALIGN.CENTER)
    s8_ids.append((pd.shape_id, 'fade'))

# 底部
bott = add_rect(s8, Inches(1), Inches(6.4), Inches(11.333), Inches(0.5), BLUE)
s8_ids.append((bott.shape_id, 'fade'))
bott_text = add_text(s8, Inches(1), Inches(6.4), Inches(11.333), Inches(0.5),
                     "让每个人都能找到理想的工作 — AI 赋能求职全流程",
                     size=16, color=BG_DARK, bold=True, font='Trebuchet MS',
                     align=PP_ALIGN.CENTER, va=MSO_ANCHOR.MIDDLE)
s8_ids.append((bott_text.shape_id, 'fade'))

add_animations(s8, s8_ids, trans='cover')


# ======================================================================
# 幻灯片 9：致谢
# ======================================================================
s9 = prs.slides.add_slide(prs.slide_layouts[6])
set_bg(s9, BG_DARK)
add_page_num(s9, 9)

s9_ids = []
add_rect(s9, Inches(5.5), Inches(2.0), Inches(2.333), Inches(0.04), BLUE)
thx = add_text(s9, Inches(1), Inches(2.3), Inches(11.333), Inches(1),
               "感谢聆听", size=52, color=WHITE, bold=True, font='Trebuchet MS',
               align=PP_ALIGN.CENTER)
s9_ids.append((thx.shape_id, 'zoom'))

sub = add_text(s9, Inches(1), Inches(3.6), Inches(11.333), Inches(0.6),
               "EasyApplyResume — AI 驱动的智能求职与管理平台",
               size=18, color=BLUE, font='Trebuchet MS', align=PP_ALIGN.CENTER)
s9_ids.append((sub.shape_id, 'fade'))

tech = add_text(s9, Inches(1), Inches(5.0), Inches(11.333), Inches(0.4),
                "Spring Boot · Spring AI · MyBatis-Plus · Redis · PostgreSQL · React · Docker",
                size=12, color=GRAY, align=PP_ALIGN.CENTER)
s9_ids.append((tech.shape_id, 'fade'))

sign = add_text(s9, Inches(1), Inches(5.6), Inches(11.333), Inches(0.4),
                "shiningCloud2025 © 2026", size=11, color=DARK_GRAY, align=PP_ALIGN.CENTER)
s9_ids.append((sign.shape_id, 'fade'))

add_animations(s9, s9_ids, trans='dissolve')


# ============ 保存 ============
out = "out/EasyApplyResume-项目介绍-v6.pptx"
prs.save(out)

# 验证
prs2 = Presentation(out)
total_anims = 0
for i, s in enumerate(prs2.slides):
    tm = s.element.find(f'{{{P_NS}}}timing')
    tr = s.element.find(f'{{{P_NS}}}transition')
    if tm is not None:
        pars = tm.findall(f'.//{{{P_NS}}}par')
        # 检查 cond 是否有 evt
        has_click = False
        for p in pars:
            for cond in p.findall(f'.//{{{P_NS}}}cond'):
                if cond.get('evt') == 'onclick':
                    has_click = True
                    break
            if has_click: break
        total_anims += len(pars)
        print(f"  Slide {i+1}: trans={tr is not None}, anims={len(pars)}, onclick={has_click}")
    else:
        print(f"  Slide {i+1}: NO timing!")

print(f"\n✅ 共 {len(prs2.slides)} 页，{total_anims} 个点击动画 | 保存到: {out}")
print("按 F5 放映 → 每点一次鼠标出现一个元素！")
