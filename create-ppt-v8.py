"""
修复动画: 确保 transition/timing 在 clrMapOvr 之前 (OOXML 元素顺序要求)
白色主题
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

# ============ 配色 ============
BG     = RGBColor(0xF8, 0xFA, 0xFC)
CARD   = RGBColor(0xFF, 0xFF, 0xFF)
BLUE   = RGBColor(0x02, 0x84, 0xC7)
TEAL   = RGBColor(0x0D, 0x94, 0x8F)
ORANGE = RGBColor(0xEA, 0x58, 0x0C)
PURPLE = RGBColor(0x7C, 0x3A, 0xED)
DARK   = RGBColor(0x0F, 0x17, 0x2A)
TEXT   = RGBColor(0x1E, 0x29, 0x3B)
GRAY   = RGBColor(0x64, 0x74, 0x8B)
WHITE  = RGBColor(0xFF, 0xFF, 0xFF)

prs = Presentation()
prs.slide_width = Inches(13.333)
prs.slide_height = Inches(7.5)
SW = prs.slide_width
SH = prs.slide_height

def set_bg(s, c): s.background.fill.solid(); s.background.fill.fore_color.rgb = c
def add_rect(s, l, t, w, h, fill, line=None):
    sh = s.shapes.add_shape(MSO_SHAPE.RECTANGLE, l, t, w, h)
    sh.fill.solid(); sh.fill.fore_color.rgb = fill
    if line: sh.line.fill.solid(); sh.line.fill.fore_color.rgb = line
    else: sh.line.fill.background()
    return sh
def add_rrect(s, l, t, w, h, fill):
    sh = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, l, t, w, h)
    sh.fill.solid(); sh.fill.fore_color.rgb = fill; sh.line.fill.background()
    return sh
def add_txt(s, l, t, w, h, text, size=12, color=TEXT, bold=False, font='Calibri', align=PP_ALIGN.LEFT, va=MSO_ANCHOR.TOP):
    tb = s.shapes.add_textbox(l, t, w, h); tf = tb.text_frame; tf.word_wrap = True
    p = tf.paragraphs[0]; p.text = text; p.font.size = Pt(size)
    p.font.color.rgb = color; p.font.bold = bold; p.font.name = font; p.alignment = align
    return tb
def title_bar(s, title):
    add_rect(s, Inches(0), Inches(0), SW, Inches(0.8), DARK)
    add_txt(s, Inches(0.6), Inches(0.15), Inches(12), Inches(0.5), title,
            size=22, color=WHITE, bold=True, font='Trebuchet MS', va=MSO_ANCHOR.MIDDLE)
    add_rect(s, Inches(0), Inches(0.8), SW, Inches(0.03), BLUE)
def page_num(s, n):
    add_txt(s, Inches(11.5), Inches(7.1), Inches(1.5), Inches(0.3), f"{n}/9", size=9, color=GRAY, align=PP_ALIGN.RIGHT)


def inject_anim(slide, trans='fade'):
    """
    正确注入动画：把 clrMapOvr 移到最后，确保 transition/timing 紧跟 cSld
    """
    sld_elem = slide.element
    
    # 找到 clrMapOvr 并移除
    clr = sld_elem.find(f'{{{P_NS}}}clrMapOvr')
    
    # 1) transition 插入在 cSld 之后
    trans_el = etree.Element(f"{{{P_NS}}}transition")
    trans_el.set("spd", "med")
    tmap = {'push':'l','cover':'r','wipe':'l','uncover':'d'}
    if trans in tmap:
        t = etree.SubElement(trans_el, f"{{{P_NS}}}{trans}")
        t.set("dir", tmap[trans])
    elif trans == 'dissolve':
        etree.SubElement(trans_el, f"{{{P_NS}}}dissolve")
    else:
        etree.SubElement(trans_el, f"{{{P_NS}}}fade")
    
    # 找到 cSld 的位置，在其后插入
    cSld = sld_elem.find(f'{{{P_NS}}}cSld')
    cSld_index = list(sld_elem).index(cSld)
    sld_elem.insert(cSld_index + 1, trans_el)
    
    # 2) timing - 获取所有 shape id
    shape_ids = [s.shape_id for s in slide.shapes]
    
    timing = etree.Element(f"{{{P_NS}}}timing")
    tnLst = etree.SubElement(timing, f"{{{P_NS}}}tnLst")
    seq = etree.SubElement(tnLst, f"{{{P_NS}}}seq"); seq.set("concurrent", "0")
    
    ct_root = etree.SubElement(seq, f"{{{P_NS}}}cTn")
    ct_root.set("id", str(uuid.uuid4())); ct_root.set("dur", "indefinite")
    scl = etree.SubElement(ct_root, f"{{{P_NS}}}stCondLst")
    c0 = etree.SubElement(scl, f"{{{P_NS}}}cond"); c0.set("delay", "0")
    
    childTnLst = etree.SubElement(seq, f"{{{P_NS}}}childTnLst")
    
    for spid in shape_ids:
        par = etree.SubElement(childTnLst, f"{{{P_NS}}}par")
        
        ct = etree.SubElement(par, f"{{{P_NS}}}cTn")
        ct.set("id", str(uuid.uuid4())); ct.set("fill", "hold")
        stc = etree.SubElement(ct, f"{{{P_NS}}}stCondLst")
        cond = etree.SubElement(stc, f"{{{P_NS}}}cond")
        cond.set("evt", "onclick"); cond.set("delay", "0")
        te = etree.SubElement(cond, f"{{{P_NS}}}tgtEl")
        etree.SubElement(te, f"{{{P_NS}}}sldTgt")
        
        cl = etree.SubElement(par, f"{{{P_NS}}}childTnLst")
        
        ae = etree.SubElement(cl, f"{{{P_NS}}}animEffect")
        ae.set("transition", "in"); ae.set("filter", "appear")
        cb = etree.SubElement(ae, f"{{{P_NS}}}cBhvr")
        cbt = etree.SubElement(cb, f"{{{P_NS}}}cTn")
        cbt.set("id", str(uuid.uuid4())); cbt.set("dur", "500")
        tgt = etree.SubElement(cb, f"{{{P_NS}}}tgtEl")
        st = etree.SubElement(tgt, f"{{{P_NS}}}spTgt"); st.set("spid", str(spid))
    
    sld_elem.insert(cSld_index + 2, timing)
    
    # 3) 如果 clrMapOvr 存在，放到最后
    if clr is not None:
        sld_elem.remove(clr)
        sld_elem.append(clr)


# ======================================================================
# 幻灯片 1: 封面
# ======================================================================
s1 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s1, BG)
add_rect(s1, Inches(1), Inches(2.0), Inches(0.06), Inches(2.5), BLUE)
add_txt(s1, Inches(1.5), Inches(2.0), Inches(10), Inches(1.0),
        "EasyApplyResume", size=52, color=DARK, bold=True, font='Trebuchet MS')
add_txt(s1, Inches(1.5), Inches(3.2), Inches(10), Inches(0.7),
        "AI 驱动的智能求职与管理平台", size=24, color=BLUE, font='Trebuchet MS')
add_rect(s1, Inches(1.5), Inches(4.2), Inches(5), Inches(0.02), TEAL)
add_txt(s1, Inches(1.5), Inches(4.5), Inches(10), Inches(0.5),
        "Spring Boot · Spring AI · MyBatis-Plus · Redis · PostgreSQL · React · Docker",
        size=13, color=GRAY)
inject_anim(s1, 'fade')

# ======================================================================
# 幻灯片 2: 问题背景
# ======================================================================
s2 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s2, BG)
title_bar(s2, "🔍 问题背景"); page_num(s2, 2)
for i, (title, desc, accent) in enumerate([
    ("简历制作低效", "海量模板难选择\n手动排版耗时长", BLUE),
    ("信息不对称", "不了解企业真实需求\nJD 与简历匹配度低", TEAL),
    ("运营负担重", "海量数据需手动管理\n缺乏智能化运营工具", ORANGE),
    ("缺乏个性化", "通用模板不匹配行业\n缺少 AI 辅助优化", PURPLE),
]):
    x = Inches(0.6 + (i%2)*6.3); y = Inches(1.2 + (i//2)*2.85)
    add_rrect(s2, x, y, Inches(5.8), Inches(2.4), CARD)
    add_rect(s2, x, y, Inches(0.06), Inches(2.4), accent)
    add_txt(s2, x+Inches(0.3), y+Inches(0.2), Inches(5.2), Inches(0.45),
            title, size=18, color=accent, bold=True, font='Trebuchet MS')
    add_txt(s2, x+Inches(0.3), y+Inches(0.85), Inches(5.2), Inches(1.3),
            desc, size=14, color=TEXT)
inject_anim(s2, 'push')

# ======================================================================
# 幻灯片 3: 技术选型
# ======================================================================
s3 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s3, BG)
title_bar(s3, "🛠️ 技术选型"); page_num(s3, 3)
for ci, (cat, items, accent) in enumerate([
    ("基础框架", ["Spring Boot 3.3.5 + Java 21", "Spring Security 双链认证",
                   "MyBatis-Plus 3.5.7", "Nacos 配置中心"], BLUE),
    ("AI & LLM", ["Spring AI 多模型适配", "ReAct Agent 智能体",
                   "RAG 检索增强生成", "DashScope / 智谱 / Ollama"], TEAL),
    ("数据与存储", ["MySQL + PostgreSQL/PgVector", "Redis 缓存 & 消息通知",
                    "MinIO / 七牛云OSS", "Docker 容器化部署"], ORANGE),
]):
    x = Inches(0.6 + ci*4.1)
    add_rect(s3, x, Inches(1.2), Inches(3.8), Inches(0.5), accent)
    add_txt(s3, x+Inches(0.1), Inches(1.2), Inches(3.6), Inches(0.5),
            cat, size=15, color=WHITE, bold=True, font='Trebuchet MS',
            align=PP_ALIGN.CENTER, va=MSO_ANCHOR.MIDDLE)
    for j, item in enumerate(items):
        iy = Inches(2.0 + j*0.55)
        add_rrect(s3, x, iy, Inches(3.8), Inches(0.45), CARD)
        add_txt(s3, x+Inches(0.15), iy+Inches(0.08), Inches(3.5), Inches(0.3),
                item, size=11, color=TEXT, va=MSO_ANCHOR.MIDDLE)
inject_anim(s3, 'cover')

# ======================================================================
# 幻灯片 4: AI 智能体架构
# ======================================================================
s4 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s4, BG)
title_bar(s4, "🧠 AI 智能体架构"); page_num(s4, 4)
for i, (name, desc, accent) in enumerate([
    ("BaseAgent", "LLM调用 · 记忆管理 · 基础抽象", BLUE),
    ("ReActAgent", "思考 → 行动 → 观察循环", TEAL),
    ("ToolCallAgent", "工具注册 · Function Calling", ORANGE),
]):
    y = Inches(1.2 + i*1.5)
    add_rrect(s4, Inches(0.6), y, Inches(5.5), Inches(1.2), CARD)
    add_rect(s4, Inches(0.6), y, Inches(0.06), Inches(1.2), accent)
    add_txt(s4, Inches(1.0), y+Inches(0.15), Inches(5), Inches(0.4),
            name, size=20, color=accent, bold=True, font='Trebuchet MS')
    add_txt(s4, Inches(1.0), y+Inches(0.6), Inches(5), Inches(0.5),
            desc, size=12, color=GRAY)
    if i<2: add_txt(s4, Inches(3), y+Inches(1.15), Inches(1), Inches(0.3),
                     "⬇", size=16, color=GRAY, align=PP_ALIGN.CENTER)
add_txt(s4, Inches(6.8), Inches(1.2), Inches(6), Inches(0.5),
        "核心组件", size=18, color=DARK, bold=True, font='Trebuchet MS')
for i, (label, desc, accent) in enumerate([
    ("RAG 知识库", "PgVector + 云知识库", BLUE),
    ("工具调用", "Web搜索 · 终端 · 文件 · PDF", TEAL),
    ("对话记忆", "MySQL 持久化 + 内存", ORANGE),
    ("安全过滤", "敏感词 · 权限控制", PURPLE),
    ("MCP 客户端", "多协议工具集成", BLUE),
    ("流式输出", "SSE 实时对话", TEAL),
]):
    y = Inches(1.85 + i*0.82)
    add_rrect(s4, Inches(6.8), y, Inches(6), Inches(0.7), CARD)
    add_rect(s4, Inches(6.8), y, Inches(0.06), Inches(0.7), accent)
    add_txt(s4, Inches(7.1), y+Inches(0.05), Inches(5.4), Inches(0.3),
            label, size=14, color=accent, bold=True, font='Trebuchet MS')
    add_txt(s4, Inches(7.1), y+Inches(0.38), Inches(5.4), Inches(0.3),
            desc, size=11, color=GRAY)
inject_anim(s4, 'wipe')

# ======================================================================
# 幻灯片 5: C端功能
# ======================================================================
s5 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s5, BG)
title_bar(s5, "📝 C端 — 简历AI助手"); page_num(s5, 5)
for i, (title, desc, accent) in enumerate([
    ("智能简历润色", "AI 分析并优化简历\n自动调整措辞与排版", BLUE),
    ("定制化建议", "根据目标 JD 提供\n针对性修改方案", TEAL),
    ("RAG 增强检索", "向量数据库匹配\n精准推荐行业知识", ORANGE),
    ("工具调用", "Web搜索 · PDF生成\n邮件发送 · 文件操作", PURPLE),
    ("流式对话", "SSE 实时响应\n打字机效果呈现", BLUE),
    ("生态服务", "MinIO/七牛云存储\n多行业模板支持", TEAL),
]):
    x = Inches(0.6 + (i%3)*4.15); y = Inches(1.2 + (i//3)*2.85)
    add_rrect(s5, x, y, Inches(3.8), Inches(2.45), CARD)
    add_rect(s5, x, y, Inches(3.8), Inches(0.05), accent)
    add_txt(s5, x+Inches(0.2), y+Inches(0.25), Inches(3.4), Inches(0.45),
            title, size=16, color=accent, bold=True, font='Trebuchet MS')
    add_txt(s5, x+Inches(0.2), y+Inches(0.9), Inches(3.4), Inches(1.3),
            desc, size=12, color=TEXT)
inject_anim(s5, 'uncover')

# ======================================================================
# 幻灯片 6: B端功能
# ======================================================================
s6 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s6, BG)
title_bar(s6, "⚙️ B端 — 系统管理助手"); page_num(s6, 6)
for i, (title, desc) in enumerate([
    ("用户管理", "注册/登录 · 权限分配 · 角色控制"),
    ("简历模板", "行业模板 CRUD · 启用/禁用"),
    ("题库管理", "题目增删改查 · 分类与难度"),
    ("FAQ管理", "常见问题维护 · 内容审核"),
    ("使用指南", "手册编辑 · 图文发布"),
]):
    y = Inches(1.15 + i*1.12)
    add_rrect(s6, Inches(0.6), y, Inches(6.5), Inches(0.95), CARD)
    add_rect(s6, Inches(0.6), y, Inches(0.06), Inches(0.95), BLUE)
    add_txt(s6, Inches(1.0), y+Inches(0.1), Inches(5.8), Inches(0.3),
            title, size=16, color=BLUE, bold=True, font='Trebuchet MS')
    add_txt(s6, Inches(1.0), y+Inches(0.45), Inches(5.8), Inches(0.4),
            desc, size=11, color=GRAY)
add_txt(s6, Inches(7.6), Inches(1.15), Inches(5.5), Inches(0.45),
        "AI 管理能力", size=18, color=TEAL, bold=True, font='Trebuchet MS')
for i, item in enumerate(["智能数据统计与分析", "自动化运营建议", "异常行为检测预警",
                           "智能问答辅助", "内容自动审核", "批量操作自动化", "日志诊断分析"]):
    add_txt(s6, Inches(7.6), Inches(1.8+i*0.5), Inches(5.5), Inches(0.4),
            f"✦ {item}", size=13, color=TEXT)
inject_anim(s6, 'fade')

# ======================================================================
# 幻灯片 7: D端功能
# ======================================================================
s7 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s7, BG)
title_bar(s7, "📊 D端 — 数据与监控平台"); page_num(s7, 7)
for i, (title, desc, accent) in enumerate([
    ("实时访问监控", "日活/总访问量追踪\nC端/B端多维数据看板", BLUE),
    ("广告投放管理", "广告上下架与排期\n用户端展示效果追踪", TEAL),
    ("服务机器监控", "SSH 远程连接管理\n健康检测与实时告警", ORANGE),
    ("智能数据报表", "Prometheus 指标采集\nGrafana 大屏可视化", PURPLE),
]):
    x = Inches(0.6 + (i%2)*6.3); y = Inches(1.2 + (i//2)*2.9)
    add_rrect(s7, x, y, Inches(5.8), Inches(2.5), CARD)
    add_rect(s7, x, y, Inches(5.8), Inches(0.05), accent)
    add_txt(s7, x+Inches(0.3), y+Inches(0.25), Inches(5.2), Inches(0.5),
            title, size=20, color=accent, bold=True, font='Trebuchet MS')
    add_txt(s7, x+Inches(0.3), y+Inches(1.0), Inches(5.2), Inches(1.3),
            desc, size=14, color=TEXT)
inject_anim(s7, 'push')

# ======================================================================
# 幻灯片 8: 未来前景
# ======================================================================
s8 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s8, BG)
title_bar(s8, "🚀 未来前景"); page_num(s8, 8)
for title, desc, accent, x in [
    ("近期目标", "完善多Agent协作\n优化RAG效果\n扩展行业模板库", BLUE, Inches(1.0)),
    ("中期规划", "上线移动端\n接入更多LLM\n开放API生态", TEAL, Inches(4.7)),
    ("远期愿景", "全链路AI求职平台\n从简历到入职\n智能闭环", ORANGE, Inches(8.4)),
]:
    add_rrect(s8, x, Inches(1.3), Inches(3.8), Inches(4.5), CARD)
    add_rect(s8, x, Inches(1.3), Inches(3.8), Inches(0.05), accent)
    add_txt(s8, x+Inches(0.3), Inches(1.6), Inches(3.2), Inches(0.5),
            title, size=22, color=accent, bold=True, font='Trebuchet MS', align=PP_ALIGN.CENTER)
    add_txt(s8, x+Inches(0.3), Inches(2.4), Inches(3.2), Inches(3),
            desc, size=16, color=TEXT, align=PP_ALIGN.CENTER)
add_rect(s8, Inches(1), Inches(6.4), Inches(11.333), Inches(0.5), BLUE)
add_txt(s8, Inches(1), Inches(6.4), Inches(11.333), Inches(0.5),
        "让每个人都能找到理想的工作 — AI 赋能求职全流程",
        size=16, color=WHITE, bold=True, font='Trebuchet MS',
        align=PP_ALIGN.CENTER, va=MSO_ANCHOR.MIDDLE)
inject_anim(s8, 'cover')

# ======================================================================
# 幻灯片 9: 致谢
# ======================================================================
s9 = prs.slides.add_slide(prs.slide_layouts[6]); set_bg(s9, BG); page_num(s9, 9)
add_rect(s9, Inches(5.5), Inches(2.0), Inches(2.333), Inches(0.04), BLUE)
add_txt(s9, Inches(1), Inches(2.3), Inches(11.333), Inches(1),
        "感谢聆听", size=52, color=DARK, bold=True, font='Trebuchet MS', align=PP_ALIGN.CENTER)
add_txt(s9, Inches(1), Inches(3.6), Inches(11.333), Inches(0.6),
        "EasyApplyResume — AI 驱动的智能求职与管理平台",
        size=18, color=BLUE, font='Trebuchet MS', align=PP_ALIGN.CENTER)
add_txt(s9, Inches(1), Inches(5.0), Inches(11.333), Inches(0.4),
        "Spring Boot · Spring AI · MyBatis-Plus · Redis · PostgreSQL · React · Docker",
        size=12, color=GRAY, align=PP_ALIGN.CENTER)
add_txt(s9, Inches(1), Inches(5.6), Inches(11.333), Inches(0.4),
        "shiningCloud2025 © 2026", size=11, color=GRAY, align=PP_ALIGN.CENTER)
inject_anim(s9, 'dissolve')


# ============ 保存 ============
out = "out/EasyApplyResume-项目介绍-v8.pptx"
prs.save(out)

# 验证: 读取 ZIP 检查元素顺序
import zipfile
with zipfile.ZipFile(out, 'r') as z:
    for fname in z.namelist():
        if fname.startswith('ppt/slides/slide'):
            with z.open(fname) as f:
                root = etree.fromstring(f.read())
                # 检查顺序
                order = [c.tag.split('}')[-1] for c in root]
                idx = fname.split('slide')[1].replace('.xml','')
                tc = sum(1 for c in order if c in ('transition','timing'))
                ok = 'transition' in order and 'timing' in order
                print(f"slide{idx}: order={order}, has_t+t={ok}, t/t_after_clr={tc==2}")
