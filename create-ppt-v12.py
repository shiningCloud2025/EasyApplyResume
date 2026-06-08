"""
最终版：中文内容 + 翻页特效 + 卡片整体一块出
"""
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE
from lxml import etree
import zipfile, uuid, copy

P = 'http://schemas.openxmlformats.org/presentationml/2006/main'
A = 'http://schemas.openxmlformats.org/drawingml/2006/main'

# 提取模板
def extract_timing_template():
    with zipfile.ZipFile('out/native-anim.pptx', 'r') as z:
        with z.open('ppt/slides/slide1.xml') as f:
            sld = etree.fromstring(f.read())
    return sld.find(f'{{{P}}}timing')

TIMING_TEMPLATE = extract_timing_template()
print(f"Template: {len(TIMING_TEMPLATE.findall(f'.//{{{P}}}par'))} pars")

def clone_timing_for_cards(card_ids):
    """
    从模板复制 timing 结构，确保有 card_ids 数量个 clickEffect
    """
    timing = copy.deepcopy(TIMING_TEMPLATE)
    
    # 找到所有 clickEffect cTn，收集它们的父 par
    all_click_ces = timing.findall(f'.//{{{P}}}cTn[@nodeType="clickEffect"]')
    click_pars = []
    for ce in all_click_ces:
        parent = ce.getparent()
        while parent is not None and parent.tag != f'{{{P}}}par':
            parent = parent.getparent()
        if parent is not None and parent not in click_pars:
            click_pars.append(parent)
    
    template_count = len(click_pars)
    need_count = len(card_ids)
    
    if need_count == 0:
        return timing
    
    parent_of_pars = click_pars[0].getparent() if click_pars else None
    
    if parent_of_pars is not None:
        # 移除多余的 click pars
        for p in click_pars[need_count:]:
            parent_of_pars.remove(p)
        
        # 添加不够的 click pars（复制最后一个作为模板）
        if need_count > template_count:
            template_par = click_pars[-1]
            for i in range(template_count, need_count):
                new_par = copy.deepcopy(template_par)
                parent_of_pars.append(new_par)
    
    # 重新收集所有 spTgt（现在数量和 card_ids 一致）
    all_targets = timing.findall(f'.//{{{P}}}spTgt')
    for i, tgt in enumerate(all_targets):
        idx = i % need_count
        if idx < need_count:
            tgt.set('spid', str(card_ids[idx]))
    
    return timing

# ============ PPT 内容 ============
prs = Presentation()
prs.slide_width = Inches(13.333)
prs.slide_height = Inches(7.5)
SW = prs.slide_width
SH = prs.slide_height

BG = CARD = RGBColor(0xFF, 0xFF, 0xFF)
BLUE = RGBColor(0x02, 0x84, 0xC7)
TEAL = RGBColor(0x0D, 0x94, 0x8F)
ORANGE = RGBColor(0xEA, 0x58, 0x0C)
PURPLE = RGBColor(0x7C, 0x3A, 0xED)
DARK = RGBColor(0x0F, 0x17, 0x2A)
TEXT = RGBColor(0x1E, 0x29, 0x3B)
GRAY = RGBColor(0x64, 0x74, 0x8B)
WHITE = RGBColor(0xFF, 0xFF, 0xFF)
LIGHT = RGBColor(0xF1, 0xF5, 0xF9)

def bg(s, c): 
    s.background.fill.solid()
    s.background.fill.fore_color.rgb = c

def rct(s, l, t, w, h, fl, li=None):
    sh = s.shapes.add_shape(MSO_SHAPE.RECTANGLE, l, t, w, h)
    sh.fill.solid()
    sh.fill.fore_color.rgb = fl
    if li:
        sh.line.fill.solid()
        sh.line.fill.fore_color.rgb = li
    else:
        sh.line.fill.background()
    return sh

def rctr(s, l, t, w, h, fl):
    sh = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, l, t, w, h)
    sh.fill.solid()
    sh.fill.fore_color.rgb = fl
    sh.line.fill.background()
    return sh

def tx(s, l, t, w, h, text, sz=12, color=TEXT, b=False, font='Calibri', al=PP_ALIGN.LEFT, va=MSO_ANCHOR.TOP):
    tb = s.shapes.add_textbox(l, t, w, h)
    tf = tb.text_frame
    tf.word_wrap = True
    p = tf.paragraphs[0]
    p.text = text
    p.font.size = Pt(sz)
    p.font.color.rgb = color
    p.font.bold = b
    p.font.name = font
    p.alignment = al
    return tb

def tbar(s, tt):
    rct(s, Inches(0), Inches(0), SW, Inches(0.8), DARK)
    tx(s, Inches(0.6), Inches(0.15), Inches(12), Inches(0.5), tt, sz=22, color=WHITE, b=True, font='Trebuchet MS', va=MSO_ANCHOR.MIDDLE)
    rct(s, Inches(0), Inches(0.8), SW, Inches(0.03), BLUE)

def pn(s, n):
    tx(s, Inches(11.5), Inches(7.1), Inches(1.5), Inches(0.3), f"{n}/9", sz=9, color=GRAY, al=PP_ALIGN.RIGHT)

# ============ 幻灯片 1: 封面 ============
s1 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s1, LIGHT)
rct(s1, Inches(1), Inches(2.0), Inches(0.06), Inches(2.5), BLUE)
tx(s1, Inches(1.5), Inches(2.0), Inches(10), Inches(1.0), "EasyApplyResume", sz=52, color=DARK, b=True, font='Trebuchet MS')
tx(s1, Inches(1.5), Inches(3.2), Inches(10), Inches(0.7), "AI 驱动的智能求职与管理平台", sz=24, color=BLUE, font='Trebuchet MS')
rct(s1, Inches(1.5), Inches(4.2), Inches(5), Inches(0.02), TEAL)
tx(s1, Inches(1.5), Inches(4.5), Inches(10), Inches(0.5), "Spring Boot . Spring AI . MyBatis-Plus . Redis . PostgreSQL . React . Docker", sz=13, color=GRAY)

# ============ 幻灯片 2: 问题背景 ============
s2 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s2, LIGHT)
tbar(s2, "问题背景")
pn(s2, 2)
cards2 = []
for i, (ti, de, ac) in enumerate([
    ("简历制作低效", "海量模板难以选择\n手动排版耗时费力", BLUE),
    ("信息不对称", "不了解企业真实需求\nJD 与简历匹配度低", TEAL),
    ("运营负担重", "海量数据需手动管理\n缺乏智能化运营工具", ORANGE),
    ("缺乏个性化", "通用模板不匹配行业\n缺少 AI 辅助优化", PURPLE),
]):
    x = Inches(0.6 + (i % 2) * 6.3)
    y = Inches(1.2 + (i // 2) * 2.85)
    card = rctr(s2, x, y, Inches(5.8), Inches(2.4), CARD)
    rct(s2, x, y, Inches(0.06), Inches(2.4), ac)
    tx(s2, x + Inches(0.3), y + Inches(0.2), Inches(5.2), Inches(0.45), ti, sz=18, color=ac, b=True, font='Trebuchet MS')
    tx(s2, x + Inches(0.3), y + Inches(0.85), Inches(5.2), Inches(1.3), de, sz=14, color=TEXT)
    cards2.append(card.shape_id)

# ============ 幻灯片 3: 技术选型 ============
s3 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s3, LIGHT)
tbar(s3, "技术选型")
pn(s3, 3)
cards3 = []
for ci, (cat, items, ac) in enumerate([
    ("基础框架", ["Spring Boot 3.3.5 + Java 21", "Spring Security 双链认证", "MyBatis-Plus 3.5.7", "Nacos 配置中心"], BLUE),
    ("AI & LLM", ["Spring AI 多模型适配", "ReAct Agent 智能体", "RAG 检索增强生成", "DashScope / 智谱 / Ollama"], TEAL),
    ("数据与存储", ["MySQL + PostgreSQL / PgVector", "Redis 缓存 & 消息通知", "MinIO / 七牛云 OSS", "Docker 容器化部署"], ORANGE),
]):
    x = Inches(0.6 + ci * 4.1)
    hdr = rct(s3, x, Inches(1.2), Inches(3.8), Inches(0.5), ac)
    tx(s3, x + Inches(0.1), Inches(1.2), Inches(3.6), Inches(0.5), cat, sz=15, color=WHITE, b=True, font='Trebuchet MS', al=PP_ALIGN.CENTER, va=MSO_ANCHOR.MIDDLE)
    cards3.append(hdr.shape_id)
    for j, item in enumerate(items):
        iy = Inches(2.0 + j * 0.55)
        item_card = rctr(s3, x, iy, Inches(3.8), Inches(0.45), CARD)
        tx(s3, x + Inches(0.15), iy + Inches(0.08), Inches(3.5), Inches(0.3), item, sz=11, color=TEXT, va=MSO_ANCHOR.MIDDLE)
        cards3.append(item_card.shape_id)

# ============ 幻灯片 4: AI 智能体架构 ============
s4 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s4, LIGHT)
tbar(s4, "AI 智能体架构")
pn(s4, 4)
cards4 = []
for i, (nm, ds, ac) in enumerate([
    ("BaseAgent", "LLM 调用 · 记忆管理 · 基础抽象", BLUE),
    ("ReActAgent", "思考 → 行动 → 观察循环", TEAL),
    ("ToolCallAgent", "工具注册 · Function Calling", ORANGE),
]):
    y = Inches(1.2 + i * 1.5)
    card = rctr(s4, Inches(0.6), y, Inches(5.5), Inches(1.2), CARD)
    rct(s4, Inches(0.6), y, Inches(0.06), Inches(1.2), ac)
    tx(s4, Inches(1.0), y + Inches(0.15), Inches(5), Inches(0.4), nm, sz=20, color=ac, b=True, font='Trebuchet MS')
    tx(s4, Inches(1.0), y + Inches(0.6), Inches(5), Inches(0.5), ds, sz=12, color=GRAY)
    cards4.append(card.shape_id)
    if i < 2:
        tx(s4, Inches(3), y + Inches(1.15), Inches(1), Inches(0.3), "⬇", sz=16, color=GRAY, al=PP_ALIGN.CENTER)

tx(s4, Inches(6.8), Inches(1.2), Inches(6), Inches(0.5), "核心组件", sz=18, color=DARK, b=True, font='Trebuchet MS')
for i, (lb, ds, ac) in enumerate([
    ("RAG 知识库", "PgVector + 云知识库", BLUE),
    ("工具调用", "Web搜索 · 终端 · 文件 · PDF", TEAL),
    ("对话记忆", "MySQL 持久化 + 内存", ORANGE),
    ("安全过滤", "敏感词 · 权限控制", PURPLE),
    ("MCP 客户端", "多协议工具集成", BLUE),
    ("流式输出", "SSE 实时对话", TEAL)
]):
    y = Inches(1.85 + i * 0.82)
    card = rctr(s4, Inches(6.8), y, Inches(6), Inches(0.7), CARD)
    rct(s4, Inches(6.8), y, Inches(0.06), Inches(0.7), ac)
    tx(s4, Inches(7.1), y + Inches(0.05), Inches(5.4), Inches(0.3), lb, sz=14, color=ac, b=True, font='Trebuchet MS')
    tx(s4, Inches(7.1), y + Inches(0.38), Inches(5.4), Inches(0.3), ds, sz=11, color=GRAY)
    cards4.append(card.shape_id)

# ============ 幻灯片 5: C端功能 ============
s5 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s5, LIGHT)
tbar(s5, "C端 — 简历AI助手")
pn(s5, 5)
cards5 = []
for i, (ti, de, ac) in enumerate([
    ("智能简历润色", "AI 分析并优化简历\n自动调整措辞与排版", BLUE),
    ("定制化建议", "根据目标 JD 提供\n针对性修改方案", TEAL),
    ("RAG 增强检索", "向量数据库匹配\n精准推荐行业知识", ORANGE),
    ("工具调用", "Web搜索 · PDF生成\n邮件发送 · 文件操作", PURPLE),
    ("流式对话", "SSE 实时响应\n打字机效果呈现", BLUE),
    ("生态服务", "MinIO / 七牛云存储\n多行业模板支持", TEAL),
]):
    x = Inches(0.6 + (i % 3) * 4.15)
    y = Inches(1.2 + (i // 3) * 2.85)
    card = rctr(s5, x, y, Inches(3.8), Inches(2.45), CARD)
    rct(s5, x, y, Inches(3.8), Inches(0.05), ac)
    tx(s5, x + Inches(0.2), y + Inches(0.25), Inches(3.4), Inches(0.45), ti, sz=16, color=ac, b=True, font='Trebuchet MS')
    tx(s5, x + Inches(0.2), y + Inches(0.9), Inches(3.4), Inches(1.3), de, sz=12, color=TEXT)
    cards5.append(card.shape_id)

# ============ 幻灯片 6: B端功能 ============
s6 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s6, LIGHT)
tbar(s6, "B端 — 系统管理助手")
pn(s6, 6)
cards6 = []
for i, (ti, de) in enumerate([
    ("用户管理", "注册/登录 · 权限分配 · 角色控制"),
    ("简历模板", "行业模板 CRUD · 启用/禁用"),
    ("题库管理", "题目增删改查 · 分类与难度"),
    ("FAQ管理", "常见问题维护 · 内容审核"),
    ("使用指南", "手册编辑 · 图文发布"),
]):
    y = Inches(1.15 + i * 1.12)
    card = rctr(s6, Inches(0.6), y, Inches(6.5), Inches(0.95), CARD)
    rct(s6, Inches(0.6), y, Inches(0.06), Inches(0.95), BLUE)
    tx(s6, Inches(1.0), y + Inches(0.1), Inches(5.8), Inches(0.3), ti, sz=16, color=BLUE, b=True, font='Trebuchet MS')
    tx(s6, Inches(1.0), y + Inches(0.45), Inches(5.8), Inches(0.4), de, sz=11, color=GRAY)
    cards6.append(card.shape_id)

tx(s6, Inches(7.6), Inches(1.15), Inches(5.5), Inches(0.45), "AI 管理能力", sz=18, color=TEAL, b=True, font='Trebuchet MS')
for i, item in enumerate(["智能数据统计与分析", "自动化运营建议", "异常行为检测预警", "智能问答辅助", "内容自动审核", "批量操作自动化", "日志诊断分析"]):
    tx(s6, Inches(7.6), Inches(1.8 + i * 0.5), Inches(5.5), Inches(0.4), f"✦ {item}", sz=13, color=TEXT)

# ============ 幻灯片 7: D端功能 ============
s7 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s7, LIGHT)
tbar(s7, "D端 — 数据与监控平台")
pn(s7, 7)
cards7 = []
for i, (ti, de, ac) in enumerate([
    ("实时访问监控", "日活/总访问量追踪\nC端/B端多维数据看板", BLUE),
    ("广告投放管理", "广告上下架与排期\n用户端展示效果追踪", TEAL),
    ("服务机器监控", "SSH 远程连接管理\n健康检测与实时告警", ORANGE),
    ("智能数据报表", "Prometheus 指标采集\nGrafana 大屏可视化", PURPLE),
]):
    x = Inches(0.6 + (i % 2) * 6.3)
    y = Inches(1.2 + (i // 2) * 2.9)
    card = rctr(s7, x, y, Inches(5.8), Inches(2.5), CARD)
    rct(s7, x, y, Inches(5.8), Inches(0.05), ac)
    tx(s7, x + Inches(0.3), y + Inches(0.25), Inches(5.2), Inches(0.5), ti, sz=20, color=ac, b=True, font='Trebuchet MS')
    tx(s7, x + Inches(0.3), y + Inches(1.0), Inches(5.2), Inches(1.3), de, sz=14, color=TEXT)
    cards7.append(card.shape_id)

# ============ 幻灯片 8: 未来前景 ============
s8 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s8, LIGHT)
tbar(s8, "未来前景")
pn(s8, 8)
cards8 = []
for ti, de, ac, x in [
    ("近期目标", "完善多Agent协作\n优化RAG效果\n扩展行业模板库", BLUE, Inches(1.0)),
    ("中期规划", "上线移动端\n接入更多LLM\n开放API生态", TEAL, Inches(4.7)),
    ("远期愿景", "全链路AI求职平台\n从简历到入职\n智能闭环", ORANGE, Inches(8.4)),
]:
    card = rctr(s8, x, Inches(1.3), Inches(3.8), Inches(4.5), CARD)
    rct(s8, x, Inches(1.3), Inches(3.8), Inches(0.05), ac)
    tx(s8, x + Inches(0.3), Inches(1.6), Inches(3.2), Inches(0.5), ti, sz=22, color=ac, b=True, font='Trebuchet MS', al=PP_ALIGN.CENTER)
    tx(s8, x + Inches(0.3), Inches(2.4), Inches(3.2), Inches(3), de, sz=16, color=TEXT, al=PP_ALIGN.CENTER)
    cards8.append(card.shape_id)

bottom = rct(s8, Inches(1), Inches(6.4), Inches(11.333), Inches(0.5), BLUE)
tx(s8, Inches(1), Inches(6.4), Inches(11.333), Inches(0.5), "让每个人都能找到理想的工作 — AI 赋能求职全流程", sz=16, color=WHITE, b=True, font='Trebuchet MS', al=PP_ALIGN.CENTER, va=MSO_ANCHOR.MIDDLE)
cards8.append(bottom.shape_id)

# ============ 幻灯片 9: 致谢 ============
s9 = prs.slides.add_slide(prs.slide_layouts[6])
bg(s9, LIGHT)
pn(s9, 9)
cards9 = []
line = rct(s9, Inches(5.5), Inches(2.0), Inches(2.333), Inches(0.04), BLUE)
tx(s9, Inches(1), Inches(2.3), Inches(11.333), Inches(1), "感谢聆听", sz=52, color=DARK, b=True, font='Trebuchet MS', al=PP_ALIGN.CENTER)
tx(s9, Inches(1), Inches(3.6), Inches(11.333), Inches(0.6), "EasyApplyResume — AI 驱动的智能求职与管理平台", sz=18, color=BLUE, font='Trebuchet MS', al=PP_ALIGN.CENTER)
tx(s9, Inches(1), Inches(5.0), Inches(11.333), Inches(0.4), "Spring Boot · Spring AI · MyBatis-Plus · Redis · PostgreSQL · React · Docker", sz=12, color=GRAY, al=PP_ALIGN.CENTER)
tx(s9, Inches(1), Inches(5.6), Inches(11.333), Inches(0.4), "shiningCloud2025 © 2026", sz=11, color=GRAY, al=PP_ALIGN.CENTER)

# ============ 保存并注入动画 ============
out = "out/EasyApplyResume-项目介绍-v12.pptx"
prs.save(out)

# ZIP 层注入动画
with zipfile.ZipFile(out, 'r') as zin:
    with zipfile.ZipFile(out + '.tmp', 'w', zipfile.ZIP_DEFLATED) as zout:
        slide_files = []
        for f in zin.namelist():
            parts = f.split('/')
            if len(parts) == 3 and parts[0] == 'ppt' and parts[1] == 'slides':
                fn = parts[2]
                if fn.startswith('slide') and fn.endswith('.xml'):
                    try:
                        int(fn[5:-4])
                        slide_files.append(f)
                    except:
                        pass
        slide_files.sort(key=lambda x: int(x.split('/')[-1][5:-4]))
        
        # 每页对应的卡片 ID 列表
        all_cards = [cards2, cards3, cards4, cards5, cards6, cards7, cards8, [], []]  # s1 和 s9 没有卡片动画
        trans_list = ['fade', 'push', 'cover', 'wipe', 'uncover', 'fade', 'push', 'cover', 'dissolve']
        
        for fname in zin.infolist():
            data = zin.read(fname.filename)
            if fname.filename in slide_files:
                idx = slide_files.index(fname.filename)
                if idx == 0 or idx == 8:  # s1 和 s9 没有卡片，用全部形状
                    sld = etree.fromstring(data)
                    # 简单处理：移除旧的，添加全部形状动画
                    for tag in ('transition', 'timing', 'clrMapOvr'):
                        el = sld.find(f'{{{P}}}{tag}')
                        if el is not None:
                            sld.remove(el)
                    
                    # 收集所有 shape ID
                    cSld = sld.find(f'{{{P}}}cSld')
                    spTree = cSld.find(f'{{{P}}}spTree')
                    all_ids = set()
                    for shape_tag in ('sp', 'grpSp', 'pic', 'graphicFrame'):
                        for el in spTree.findall(f'{{{P}}}{shape_tag}'):
                            for nv_tag in ('nvSpPr', 'nvGrpSpPr', 'nvPicPr', 'nvGraphicFramePr', 'nvCxnSpPr'):
                                nv = el.find(f'{{{P}}}{nv_tag}')
                                if nv is not None:
                                    cnv = nv.find(f'{{{P}}}cNvPr')
                                    if cnv is not None:
                                        sid = cnv.get('id')
                                        if sid:
                                            all_ids.add(int(sid))
                    all_ids = sorted(all_ids)
                    
                    timing = clone_timing_for_cards(all_ids)
                    sld.append(timing)
                    
                    # 添加翻页特效
                    trans = etree.Element(f'{{{P}}}transition', spd='med')
                    if trans_list[idx] in ('push', 'cover', 'wipe', 'uncover'):
                        tmap = {'push': 'l', 'cover': 'r', 'wipe': 'l', 'uncover': 'd'}
                        t = etree.SubElement(trans, f'{{{P}}}{trans_list[idx]}')
                        t.set('dir', tmap[trans_list[idx]])
                    elif trans_list[idx] == 'dissolve':
                        etree.SubElement(trans, f'{{{P}}}dissolve')
                    else:
                        etree.SubElement(trans, f'{{{P}}}fade')
                    
                    # 插入 transition 在 cSld 之后
                    cSld_idx = list(sld).index(cSld)
                    sld.insert(cSld_idx + 1, trans)
                    
                    # clrMapOvr 放最后
                    clr = etree.Element(f'{{{P}}}clrMapOvr')
                    sld.append(clr)
                    
                    data = etree.tostring(sld, xml_declaration=True, encoding='UTF-8', standalone=True)
                else:
                    # 卡片页
                    card_ids = all_cards[idx - 1] if idx - 1 < len(all_cards) else []
                    sld = etree.fromstring(data)
                    for tag in ('transition', 'timing', 'clrMapOvr'):
                        el = sld.find(f'{{{P}}}{tag}')
                        if el is not None:
                            sld.remove(el)
                    
                    timing = clone_timing_for_cards(card_ids)
                    sld.append(timing)
                    
                    # 翻页特效
                    trans = etree.Element(f'{{{P}}}transition', spd='med')
                    if trans_list[idx] in ('push', 'cover', 'wipe', 'uncover'):
                        tmap = {'push': 'l', 'cover': 'r', 'wipe': 'l', 'uncover': 'd'}
                        t = etree.SubElement(trans, f'{{{P}}}{trans_list[idx]}')
                        t.set('dir', tmap[trans_list[idx]])
                    elif trans_list[idx] == 'dissolve':
                        etree.SubElement(trans, f'{{{P}}}dissolve')
                    else:
                        etree.SubElement(trans, f'{{{P}}}fade')
                    
                    cSld = sld.find(f'{{{P}}}cSld')
                    cSld_idx = list(sld).index(cSld)
                    sld.insert(cSld_idx + 1, trans)
                    
                    clr = etree.Element(f'{{{P}}}clrMapOvr')
                    sld.append(clr)
                    
                    data = etree.tostring(sld, xml_declaration=True, encoding='UTF-8', standalone=True)
            zout.writestr(fname, data)

import os
os.replace(out + '.tmp', out)

# 验证
with zipfile.ZipFile(out, 'r') as z:
    for f in sorted(z.namelist()):
        parts = f.split('/')
        if len(parts) == 3 and parts[0] == 'ppt' and parts[1] == 'slides':
            fn = parts[2]
            if fn.startswith('slide') and fn.endswith('.xml'):
                try:
                    int(fn[5:-4])
                except:
                    continue
                with z.open(f) as ff:
                    sld = etree.fromstring(ff.read())
                    tm = sld.find(f'{{{P}}}timing')
                    ck = len(tm.findall(f'.//{{{P}}}cTn[@nodeType="clickEffect"]')) if tm is not None else 0
                    tr = sld.find(f'{{{P}}}transition')
                    tr_type = tr[0].tag.split('}')[-1] if tr is not None and len(tr) > 0 else 'none'
                    print(f"{fn}: clickEffects={ck}, transition={tr_type}")

print(f"\nSaved: {out}")
