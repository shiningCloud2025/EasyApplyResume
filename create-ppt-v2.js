const pptxgen = require("pptxgenjs");

const pres = new pptxgen();
pres.layout = "LAYOUT_16x9";
pres.author = "shiningCloud2025";
pres.title = "EasyApplyResume - 易投简历智能平台";

// ============================================================
// Unified Color Palette — 统一配色
// ============================================================
const C = {
  bg:       "F8FAFC",   // 统一浅色背景
  white:    "FFFFFF",
  primary:  "1E3A5F",   // 深蓝（标题/强调）
  accent:   "0EA5E9",   // 天蓝（装饰/高亮）
  teal:     "14B8A6",   // 青色（第二强调）
  textDark: "1E293B",   // 深色正文
  textMid:  "475569",   // 中灰色
  textLight:"94A3B8",   // 浅灰
  cardBg:   "FFFFFF",
  codeBg:   "0C4A6E",   // 代码背景（保持深色便于阅读代码）
  border:   "E2E8F0",
};

const FONT_H = "Trebuchet MS";
const FONT_B = "Calibri";

// ============================================================
// Shared helpers
// ============================================================
function makeShadow() {
  return { type: "outer", color: "000000", blur: 6, offset: 2, angle: 135, opacity: 0.08 };
}

// 统一顶部标题栏
function addHeader(slide, title, subtitle) {
  // 顶部装饰条
  slide.addShape(pres.shapes.RECTANGLE, {
    x: 0, y: 0, w: 10, h: 0.06,
    fill: { color: C.accent },
  });
  // 标题
  slide.addText(title, {
    x: 0.6, y: 0.2, w: 8.8, h: 0.55,
    fontSize: 28, fontFace: FONT_H, color: C.primary,
    bold: true, margin: 0,
  });
  // 副标题
  if (subtitle) {
    slide.addText(subtitle, {
      x: 0.6, y: 0.72, w: 8.8, h: 0.3,
      fontSize: 11, fontFace: FONT_B, color: C.textMid,
      margin: 0,
    });
  }
  // 标题下方分割线
  slide.addShape(pres.shapes.LINE, {
    x: 0.6, y: 1.05, w: 8.8, h: 0,
    line: { color: C.border, width: 1 },
  });
}

// 统一底部页码
function addFooter(slide, pageNum) {
  slide.addShape(pres.shapes.LINE, {
    x: 0.5, y: 5.25, w: 9.0, h: 0,
    line: { color: C.border, width: 0.5 },
  });
  slide.addText(`EasyApplyResume · ${pageNum}`, {
    x: 0.5, y: 5.3, w: 9, h: 0.25,
    fontSize: 7.5, fontFace: FONT_B, color: C.textLight,
    align: "right", valign: "middle",
  });
}

// 统一卡片（带阴影白底）
function addCard(slide, x, y, w, h, accentColor) {
  slide.addShape(pres.shapes.RECTANGLE, {
    x, y, w, h,
    fill: { color: C.cardBg },
    shadow: makeShadow(),
  });
  if (accentColor) {
    slide.addShape(pres.shapes.RECTANGLE, {
      x, y, w: 0.05, h,
      fill: { color: accentColor },
    });
  }
}

// ============================================================
// SLIDE 1: 封面 (special dark treatment)
// ============================================================
const s1 = pres.addSlide();
s1.background = { color: "0F172A" };

// Top decorative bar
s1.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 10, h: 0.08,
  fill: { color: C.teal },
});

// Decorative diagonal shapes
s1.addShape(pres.shapes.RECTANGLE, {
  x: 6.8, y: 0, w: 3.2, h: 5.625,
  fill: { color: "1A2F4A", transparency: 40 },
});

// Accent lines
s1.addShape(pres.shapes.LINE, {
  x: 1.2, y: 2.55, w: 2.2, h: 0,
  line: { color: C.teal, width: 3 },
});

// Title
s1.addText("EasyApplyResume", {
  x: 1.2, y: 1.2, w: 7.5, h: 0.9,
  fontSize: 42, fontFace: FONT_H, color: C.white,
  bold: true, margin: 0,
});

// Subtitle
s1.addText("易投简历 — AI 智能求职与简历管理平台", {
  x: 1.2, y: 2.15, w: 7.5, h: 0.45,
  fontSize: 19, fontFace: FONT_B, color: "7DD3FC",
  margin: 0,
});

// Tagline
s1.addText("Spring AI · ReAct Agent · RAG 知识库 · 多模型集成", {
  x: 1.2, y: 2.75, w: 7.5, h: 0.4,
  fontSize: 13, fontFace: FONT_B, color: C.textLight,
  margin: 0,
});

// Separator
s1.addShape(pres.shapes.LINE, {
  x: 1.2, y: 3.4, w: 3.0, h: 0,
  line: { color: C.accent, width: 1.2 },
});

// Info text
s1.addText([
  { text: "技术架构：", options: { bold: true, color: C.white } },
  { text: "Spring Boot 3.3.5 + Java 21 + MySQL + PostgreSQL + Redis\n", options: { color: C.textLight, breakLine: true } },
  { text: "AI 能力：", options: { bold: true, color: C.white } },
  { text: "Spring AI + DashScope + 智谱AI + 豆包 + Ollama\n", options: { color: C.textLight, breakLine: true } },
  { text: "部署方式：", options: { bold: true, color: C.white } },
  { text: "Docker + Nacos + Spring Boot Admin + Prometheus", options: { color: C.textLight } },
], {
  x: 1.2, y: 3.65, w: 5.3, h: 1.5,
  fontSize: 11.5, fontFace: FONT_B, lineSpacingMultiple: 1.4, margin: 0,
});

// Right side colored bars
s1.addShape(pres.shapes.RECTANGLE, { x: 7.8, y: 1.0, w: 1.5, h: 0.05, fill: { color: C.teal } });
s1.addShape(pres.shapes.RECTANGLE, { x: 7.8, y: 1.25, w: 1.5, h: 0.05, fill: { color: C.accent } });
s1.addShape(pres.shapes.RECTANGLE, { x: 7.8, y: 1.5, w: 1.5, h: 0.05, fill: { color: C.teal } });

s1.addText("作者：shiningCloud2025 | 2026年6月", {
  x: 1.2, y: 5.2, w: 4, h: 0.25,
  fontSize: 9, fontFace: FONT_B, color: "475569", margin: 0,
});

// ============================================================
// SLIDE 2: 问题背景
// ============================================================
const s2 = pres.addSlide();
s2.background = { color: C.bg };

addHeader(s2, "问题背景", "当前求职市场的核心痛点与 AI 破局之道");

const painPoints = [
  { title: "简历制作低效", desc: "求职者缺乏专业指导，简历内容空洞、结构混乱、表达不专业，严重影响面试邀约率", icon: "📝" },
  { title: "求职信息不对称", desc: "求职者难以获取全面的职位信息、行业动态和公司评价，缺乏有效的决策依据", icon: "🔍" },
  { title: "管理者运营负担重", desc: "HR 和管理员面临大量重复性的简历筛选、用户管理、数据统计工作，效率低下", icon: "⚙️" },
  { title: "缺乏个性化服务", desc: "传统求职平台千篇一律，无法根据个人背景和目标岗位提供量身定制的优化建议", icon: "🎯" },
];

painPoints.forEach((pp, i) => {
  const col = i % 2;
  const row = Math.floor(i / 2);
  const x = 0.6 + col * 4.65;
  const y = 1.3 + row * 1.95;

  addCard(s2, x, y, 4.3, 1.72, row === 0 ? C.accent : C.teal);

  s2.addText(pp.icon + "  " + pp.title, {
    x: x + 0.25, y: y + 0.15, w: 3.8, h: 0.35,
    fontSize: 14, fontFace: FONT_H, color: C.textDark,
    bold: true, margin: 0,
  });
  s2.addText(pp.desc, {
    x: x + 0.25, y: y + 0.6, w: 3.8, h: 0.95,
    fontSize: 11, fontFace: FONT_B, color: C.textMid,
    margin: 0, lineSpacingMultiple: 1.3,
  });
});

// Bottom highlight bar
s2.addShape(pres.shapes.RECTANGLE, {
  x: 0.6, y: 5.0, w: 8.8, h: 0.25,
  fill: { color: C.accent, transparency: 88 },
});
s2.addText("核心思路：基于 Spring AI 生态，构建 AI Agent 驱动的智能求职助手，实现简历优化、智能问答、自动化运营", {
  x: 0.75, y: 5.0, w: 8.5, h: 0.25,
  fontSize: 10, fontFace: FONT_B, color: C.primary, italic: true,
  valign: "middle", margin: 0,
});

addFooter(s2, "2 / 9");

// ============================================================
// SLIDE 3: 技术选型
// ============================================================
const s3 = pres.addSlide();
s3.background = { color: C.bg };

addHeader(s3, "技术选型", "Spring Boot 3.3.5 + Java 21 微服务架构，多层次 AI 能力集成");

const techCats = [
  {
    title: "基础框架",
    color: C.accent,
    items: ["Spring Boot 3.3.5 (Java 21)", "MyBatis-Plus 3.5.7", "Spring Security + JWT", "Nacos 配置/服务发现", "Docker 容器化部署"],
  },
  {
    title: "AI & LLM",
    color: C.teal,
    items: ["Spring AI 1.0.0-M6", "通义千问 (DashScope)", "智谱AI (GLM-4)", "豆包 (OpenAI 协议)", "Ollama 本地模型"],
  },
  {
    title: "数据与存储",
    color: C.primary,
    items: ["MySQL 8.0 (主库)", "PostgreSQL + PgVector", "Redis (缓存/JWT)", "MinIO / 七牛云 OSS", "Spring Data Redis"],
  },
];

techCats.forEach((cat, i) => {
  const x = 0.5 + i * 3.2;
  const y = 1.35;

  // Category header
  s3.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.95, h: 0.42,
    fill: { color: cat.color },
  });
  s3.addText(cat.title, {
    x: x, y: y, w: 2.95, h: 0.42,
    fontSize: 13, fontFace: FONT_H, color: C.white,
    bold: true, align: "center", valign: "middle", margin: 0,
  });

  cat.items.forEach((item, j) => {
    addCard(s3, x, y + 0.55 + j * 0.56, 2.95, 0.48);
    s3.addText(item, {
      x: x + 0.18, y: y + 0.55 + j * 0.56, w: 2.6, h: 0.48,
      fontSize: 10.5, fontFace: FONT_B, color: C.textDark,
      valign: "middle", margin: 0,
    });
  });
});

// Bottom overview
s3.addShape(pres.shapes.RECTANGLE, {
  x: 0.5, y: 4.38, w: 9.0, h: 0.55,
  fill: { color: C.cardBg },
  shadow: makeShadow(),
});
s3.addShape(pres.shapes.RECTANGLE, {
  x: 0.5, y: 4.38, w: 0.05, h: 0.55,
  fill: { color: C.teal },
});
s3.addText([
  { text: "AI 能力矩阵：", options: { bold: true, color: C.textDark } },
  { text: "ReAct Agent · RAG 检索增强 · MCP 客户端 · 多模型适配 · 向量数据库 · Function Calling · 对话记忆 · 敏感词过滤", options: { color: C.textMid } },
], {
  x: 0.75, y: 4.4, w: 8.5, h: 0.5,
  fontSize: 11, fontFace: FONT_B, margin: 0, valign: "middle",
});

addFooter(s3, "3 / 9");

// ============================================================
// SLIDE 4: AI 智能体架构
// ============================================================
const s4 = pres.addSlide();
s4.background = { color: C.bg };

addHeader(s4, "AI 智能体架构设计", "自研 ReAct Agent 模式：思考→行动循环 + 工具调用 + RAG 增强");

// Left: Agent hierarchy
s4.addShape(pres.shapes.RECTANGLE, {
  x: 0.6, y: 1.3, w: 4.5, h: 3.65,
  fill: { color: C.cardBg },
  shadow: makeShadow(),
});
s4.addText("Agent 继承体系", {
  x: 0.8, y: 1.38, w: 4.1, h: 0.32,
  fontSize: 13, fontFace: FONT_H, color: C.primary, bold: true, margin: 0,
});

const agentLayers = [
  { label: "BaseAgent", desc: "状态管理 · 步骤循环 · 流式 SSE · 资源清理", w: 3.2, color: C.primary },
  { label: "ReActAgent", desc: "Think & Act 模式 · 思考-行动循环 · 步骤编排", w: 3.6, color: "2563EB" },
  { label: "ToolCallAgent", desc: "工具调用管理 · 对话上下文维护 · 终止判断", w: 4.0, color: C.accent },
];

agentLayers.forEach((layer, i) => {
  const y = 1.9 + i * 1.1;
  const boxWidth = layer.w;
  const boxX = 0.85 + (4.0 - boxWidth) / 2;
  s4.addShape(pres.shapes.RECTANGLE, {
    x: boxX, y: y, w: boxWidth, h: 0.75,
    fill: { color: layer.color },
    shadow: makeShadow(),
  });
  s4.addText(layer.label, {
    x: boxX, y: y + 0.04, w: boxWidth, h: 0.32,
    fontSize: 12, fontFace: FONT_H, color: C.white,
    bold: true, align: "center", valign: "middle", margin: 0,
  });
  s4.addText(layer.desc, {
    x: boxX, y: y + 0.38, w: boxWidth, h: 0.28,
    fontSize: 8.5, fontFace: FONT_B, color: "CBD5E1",
    align: "center", valign: "middle", margin: 0,
  });

  if (i < agentLayers.length - 1) {
    s4.addShape(pres.shapes.LINE, {
      x: 2.85, y: y + 0.75, w: 0, h: 0.35,
      line: { color: C.textLight, width: 1.2, dashType: "dash" },
    });
  }
});

// Right: Components
s4.addShape(pres.shapes.RECTANGLE, {
  x: 5.35, y: 1.3, w: 4.15, h: 3.65,
  fill: { color: C.cardBg },
  shadow: makeShadow(),
});
s4.addText("核心组件", {
  x: 5.55, y: 1.38, w: 3.75, h: 0.32,
  fontSize: 13, fontFace: FONT_H, color: C.primary, bold: true, margin: 0,
});

const components = [
  { label: "ChatMemory", desc: "MySQL + 内存混合持久化，多轮对话上下文" },
  { label: "RAG Advisor", desc: "PgVector 向量库 + 云知识库 + 查询重写" },
  { label: "Tools（8个）", desc: "网页搜索 · 终端操作 · 文件管理 · PDF生成 · 邮件" },
  { label: "敏感词过滤", desc: "用户端/管理端双套 Advisor，覆盖百种违规类型" },
  { label: "MCP Client", desc: "Stdio / SSE 协议 MCP 工具集成" },
  { label: "LLM Router", desc: "DashScope / 智谱 / 豆包 / Ollama 自由切换" },
];

components.forEach((comp, i) => {
  const y = 1.85 + i * 0.48;
  addCard(s4, 5.55, y, 3.75, 0.42, i % 2 === 0 ? C.accent : C.teal);
  s4.addText(comp.label, {
    x: 5.78, y: y + 0.02, w: 3.35, h: 0.20,
    fontSize: 10.5, fontFace: FONT_H, color: C.primary,
    bold: true, margin: 0,
  });
  s4.addText(comp.desc, {
    x: 5.78, y: y + 0.22, w: 3.35, h: 0.17,
    fontSize: 8, fontFace: FONT_B, color: C.textMid,
    margin: 0,
  });
});

// Bottom note
s4.addText([
  { text: "ResumeAssistantAgent", options: { bold: true, color: C.accent } },
  { text: " — C端简历助手　　　", options: { color: C.textDark } },
  { text: "SystemAssistantAgent", options: { bold: true, color: C.teal } },
  { text: " — B端管理助手", options: { color: C.textDark } },
], {
  x: 0.8, y: 4.97, w: 8.5, h: 0.22,
  fontSize: 10, fontFace: FONT_B, margin: 0,
});

addFooter(s4, "4 / 9");

// ============================================================
// SLIDE 5: C端功能
// ============================================================
const s5 = pres.addSlide();
s5.background = { color: C.bg };

addHeader(s5, "核心功能：C端 — AI 简历助手", "面向求职者的全流程 AI 辅助：从简历优化到求职决策");

const cFeatures = [
  { title: "智能简历润色", items: "语法优化 · 专业表达 · 结构重组\n量化成果 · STAR 法则改写" },
  { title: "定制化建议", items: "按行业/岗位定制 · 保研/留学适配\n多版本输出 · 关键词优化" },
  { title: "RAG 知识增强", items: "PgVector 向量库 · 云知识库检索\n查询重写优化 · 实时行业数据" },
  { title: "工具调用能力", items: "联网搜索 · 简历 PDF 生成\n邮件发送 · 网页数据抓取" },
  { title: "流式对话体验", items: "SSE 实时流式输出 · 多轮上下文\n混合持久化记忆 · 敏感词过滤" },
  { title: "求职生态服务", items: "职位/收藏 · 简历模板 · 面试题库\n行业资讯 · 地区数据查询" },
];

cFeatures.forEach((feat, i) => {
  const col = i % 3;
  const row = Math.floor(i / 3);
  const x = 0.5 + col * 3.15;
  const y = 1.3 + row * 2.0;

  addCard(s5, x, y, 2.95, 1.78, row === 0 ? C.accent : C.teal);

  // Top accent bar (full width overlay on the card top)
  s5.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.95, h: 0.05,
    fill: { color: row === 0 ? C.accent : C.teal },
  });

  s5.addText(feat.title, {
    x: x + 0.2, y: y + 0.15, w: 2.55, h: 0.35,
    fontSize: 13, fontFace: FONT_H, color: C.primary,
    bold: true, margin: 0,
  });
  s5.addText(feat.items, {
    x: x + 0.2, y: y + 0.6, w: 2.55, h: 1.0,
    fontSize: 10, fontFace: FONT_B, color: C.textMid,
    margin: 0, lineSpacingMultiple: 1.3,
  });
});

addFooter(s5, "5 / 9");

// ============================================================
// SLIDE 6: B端功能
// ============================================================
const s6 = pres.addSlide();
s6.background = { color: C.bg };

addHeader(s6, "核心功能：B端 — AI 系统管理助手", "面向管理员的 AI 运维辅助：权限管理 · 内容运营 · 数据监控 · 智能诊断");

// Left: Admin modules
const adminModules = [
  { title: "管理员管理", desc: "增删改查 · RBAC 角色权限分配 · 账户状态管理 · 登录日志" },
  { title: "内容运营", desc: "公告/广告管理 · FAQ 问答库 · 项目/团队介绍 · 合作伙伴 · 媒体报道" },
  { title: "数据管理", desc: "省市区地图 · 行业分类 · 大学库 · 职位库 · 简历模板 · 求职攻略" },
  { title: "AI 运营工具", desc: "评分模型训练数据 · 模型版本管理 · LLM 工具配置 · 邮件通信群发" },
  { title: "监控统计", desc: "日活/访问量统计 · 服务机器监控 · Prometheus + Grafana 指标体系" },
];

adminModules.forEach((mod, i) => {
  const y = 1.3 + i * 0.75;
  addCard(s6, 0.5, y, 5.3, 0.65, C.accent);
  s6.addText(mod.title, {
    x: 0.8, y: y + 0.05, w: 4.8, h: 0.26,
    fontSize: 12, fontFace: FONT_H, color: C.primary, bold: true, margin: 0,
  });
  s6.addText(mod.desc, {
    x: 0.8, y: y + 0.33, w: 4.8, h: 0.26,
    fontSize: 9.5, fontFace: FONT_B, color: C.textMid, margin: 0,
  });
});

// Right: AI management features
s6.addShape(pres.shapes.RECTANGLE, {
  x: 6.1, y: 1.3, w: 3.5, h: 3.6,
  fill: { color: C.cardBg },
  shadow: makeShadow(),
});
s6.addShape(pres.shapes.RECTANGLE, {
  x: 6.1, y: 1.3, w: 3.5, h: 0.45,
  fill: { color: C.primary },
});
s6.addText("AI 管理助手能力", {
  x: 6.1, y: 1.3, w: 3.5, h: 0.45,
  fontSize: 13, fontFace: FONT_H, color: C.white,
  bold: true, align: "center", valign: "middle", margin: 0,
});

const aiAdminFeatures = [
  "引导式故障诊断与排查",
  "系统配置与运维指导",
  "数据报表智能解读",
  "用户/简历问题快速定位",
  "MCP + RAG + 工具调用增强",
  "独立对话记忆管理",
  "管理端专属敏感词过滤",
  "Re2 重读增强理解",
];

aiAdminFeatures.forEach((feat, i) => {
  const y = 1.95 + i * 0.36;
  s6.addShape(pres.shapes.RECTANGLE, {
    x: 6.25, y: y + 0.07, w: 0.18, h: 0.18,
    fill: { color: C.accent },
  });
  s6.addText(feat, {
    x: 6.55, y: y, w: 2.85, h: 0.33,
    fontSize: 10, fontFace: FONT_B, color: C.textDark,
    valign: "middle", margin: 0,
  });
});

addFooter(s6, "6 / 9");

// ============================================================
// SLIDE 7: 代码实现
// ============================================================
const s7 = pres.addSlide();
s7.background = { color: C.bg };

addHeader(s7, "核心代码实现", "ReAct Agent 思考执行 · 工具调用 · 双安全过滤链");

// Left code panel - ToolCallAgent
const code1 = `// ToolCallAgent.java — think() 思考阶段
@Override
public boolean think() {
  if (getNextStepPrompt() != null
      && !getNextStepPrompt().isEmpty()) {
    getMessageList().add(
        new UserMessage(getNextStepPrompt()));
  }
  Prompt prompt = new Prompt(
      getMessageList(), chatOptions);
  ChatResponse chatResponse = getChatClient()
      .prompt(prompt)
      .system(getSystemPrompt())
      .tools(availableTools)
      .call()
      .chatResponse();
  this.toolCallChatResponse = chatResponse;
  AssistantMessage assistantMessage =
      chatResponse.getResult().getOutput();
  List<ToolCall> toolCallsList =
      assistantMessage.getToolCalls();
  if (toolCallsList.isEmpty()) {
    getMessageList().add(assistantMessage);
    return false;  // 无需工具调用
  }
  return true;  // 需要执行工具
}`;

s7.addShape(pres.shapes.RECTANGLE, {
  x: 0.4, y: 1.2, w: 4.55, h: 3.7,
  fill: { color: "0C1B33" },
  shadow: makeShadow(),
});
s7.addText("ToolCallAgent.think() 方法", {
  x: 0.55, y: 1.23, w: 4.2, h: 0.25,
  fontSize: 9, fontFace: "Consolas", color: C.teal,
  bold: true, margin: 0,
});
s7.addText(code1, {
  x: 0.55, y: 1.5, w: 4.2, h: 3.3,
  fontSize: 7.8, fontFace: "Consolas", color: "CBD5E1",
  margin: 0, lineSpacingMultiple: 1.22,
});

// Right code panel - Security Config
const code2 = `// 双 SecurityFilterChain 配置
@Configuration @EnableWebSecurity @Order(1)
public class UserSecurityConfig {
 @Bean
 public SecurityFilterChain
     userSecurityFilterChain(HttpSecurity http){
   http
    .securityMatcher("/user/**")
    .cors(...)
    .csrf(AbstractHttpConfigurer::disable)
    .sessionManagement(session->session
      .sessionCreationPolicy(
        SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth->auth
      .requestMatchers(
        "/user/auth/**").permitAll()
      .requestMatchers(
        "/user/sms/**").permitAll()
      .anyRequest().authenticated())
    .exceptionHandling(ex->ex
      .authenticationEntryPoint(...))
    .addFilterBefore(userJwtAuthFilter,
      UsernamePasswordAuthFilter.class);
   return http.build();
 }
}`;

s7.addShape(pres.shapes.RECTANGLE, {
  x: 5.05, y: 1.2, w: 4.55, h: 3.7,
  fill: { color: "0C1B33" },
  shadow: makeShadow(),
});
s7.addText("UserSecurityConfig 安全配置", {
  x: 5.2, y: 1.23, w: 4.2, h: 0.25,
  fontSize: 9, fontFace: "Consolas", color: C.teal,
  bold: true, margin: 0,
});
s7.addText(code2, {
  x: 5.2, y: 1.5, w: 4.2, h: 3.3,
  fontSize: 7.8, fontFace: "Consolas", color: "CBD5E1",
  margin: 0, lineSpacingMultiple: 1.22,
});

// Bottom note
s7.addShape(pres.shapes.RECTANGLE, {
  x: 0.4, y: 4.97, w: 9.2, h: 0.22,
  fill: { color: C.accent, transparency: 85 },
});
s7.addText("以上代码均来自项目真实源码：ToolCallAgent.java（think 方法） 与 UserSecurityConfig.java（双 FilterChain 安全配置）", {
  x: 0.55, y: 4.97, w: 8.9, h: 0.22,
  fontSize: 8, fontFace: FONT_B, color: C.primary, italic: true,
  valign: "middle", margin: 0,
});

addFooter(s7, "7 / 9");

// ============================================================
// SLIDE 8: 未来前景
// ============================================================
const s8 = pres.addSlide();
s8.background = { color: C.bg };

addHeader(s8, "未来前景", "持续迭代 · 拥抱 AI 前沿 · 打造全链路智能求职生态");

const futurePlans = [
  {
    phase: "近期 (1-3月)",
    color: C.accent,
    items: ["前端界面开发完成", "多租户 SaaS 化改造", "接入更多 LLM（DeepSeek 等）", "简历评分模型上线 (XGBoost)"],
  },
  {
    phase: "中期 (3-6月)",
    color: C.teal,
    items: ["微服务化拆分 (Spring Cloud)", "全链路可观测性增强", "智能岗位匹配推荐", "移动端适配（小程序）"],
  },
  {
    phase: "远期 (6-12月)",
    color: C.primary,
    items: ["多 Agent 协作架构", "AI 模拟面试与反馈", "企业端 AI 筛选助手", "构建求职知识图谱"],
  },
];

futurePlans.forEach((plan, i) => {
  const x = 0.4 + i * 3.2;
  const y = 1.35;

  // Phase header
  s8.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.95, h: 0.45,
    fill: { color: plan.color },
  });
  s8.addText(plan.phase, {
    x: x, y: y, w: 2.95, h: 0.45,
    fontSize: 13, fontFace: FONT_H, color: C.white,
    bold: true, align: "center", valign: "middle", margin: 0,
  });

  plan.items.forEach((item, j) => {
    addCard(s8, x, y + 0.58 + j * 0.6, 2.95, 0.52, plan.color);
    s8.addText(item, {
      x: x + 0.2, y: y + 0.58 + j * 0.6, w: 2.55, h: 0.52,
      fontSize: 10.5, fontFace: FONT_B, color: C.textDark,
      valign: "middle", margin: 0,
    });
  });
});

// Vision banner
s8.addShape(pres.shapes.RECTANGLE, {
  x: 0.4, y: 4.45, w: 9.2, h: 0.55,
  fill: { color: C.primary },
});
s8.addText("愿景：成为求职者最信赖的 AI 求职伙伴，用人均 AI 赋能每位求职者找到理想工作", {
  x: 0.6, y: 4.45, w: 8.8, h: 0.55,
  fontSize: 14, fontFace: FONT_H, color: C.white,
  align: "center", valign: "middle", margin: 0,
  bold: true,
});

addFooter(s8, "8 / 9");

// ============================================================
// SLIDE 9: 致谢
// ============================================================
const s9 = pres.addSlide();
s9.background = { color: "0F172A" };

// Top + bottom decorative bars
s9.addShape(pres.shapes.RECTANGLE, { x: 0, y: 0, w: 10, h: 0.08, fill: { color: C.teal } });
s9.addShape(pres.shapes.RECTANGLE, { x: 0, y: 5.545, w: 10, h: 0.08, fill: { color: C.teal } });

s9.addText("致  谢", {
  x: 1, y: 1.1, w: 8, h: 1.0,
  fontSize: 44, fontFace: FONT_H, color: C.white,
  bold: true, align: "center", valign: "middle", margin: 0,
});

s9.addShape(pres.shapes.LINE, {
  x: 4.0, y: 2.15, w: 2.0, h: 0,
  line: { color: C.teal, width: 2.5 },
});

s9.addText([
  { text: "感谢各位老师和同学对 EasyApplyResume 项目的关注与支持\n\n", options: { breakLine: true, color: "CBD5E1", fontSize: 15 } },
  { text: "本项目基于 Spring AI 生态，探索了 AI Agent 在求职领域的应用\n", options: { breakLine: true, color: "94A3B8", fontSize: 12 } },
  { text: "从自研 ReAct Agent 到 RAG 增强检索，从多模型适配到工具集成\n", options: { breakLine: true, color: "94A3B8", fontSize: 12 } },
  { text: "这是一次将前沿 AI 技术与实际业务场景结合的有益尝试\n\n", options: { breakLine: true, color: "94A3B8", fontSize: 12 } },
  { text: "技术栈：", options: { bold: true, color: C.teal, fontSize: 12 } },
  { text: "Spring Boot 3.3.5 · Spring AI · MyBatis-Plus · PgVector · Redis · Nacos · Docker\n", options: { breakLine: true, color: "94A3B8", fontSize: 12 } },
  { text: "AI 模型：", options: { bold: true, color: C.teal, fontSize: 12 } },
  { text: "通义千问 · 智谱AI (GLM-4) · 豆包 · Ollama\n\n", options: { breakLine: true, color: "94A3B8", fontSize: 12 } },
  { text: "📧 shining_cloud2025@163.com\n", options: { breakLine: true, color: "7DD3FC", fontSize: 12 } },
  { text: "🐙 github.com/shiningCloud2025", options: { color: "7DD3FC", fontSize: 12 } },
], {
  x: 1.5, y: 2.5, w: 7, h: 2.8,
  fontFace: FONT_B,
  align: "center", valign: "top", margin: 0,
  lineSpacingMultiple: 1.3,
});

// ============================================================
// SLIDE TRANSITIONS
// ============================================================
// 为每页设置不同的切换动画
const transitions = [
  null,                  // slide 1 (封面无切换)
  { type: "push", dir: "l", dur: 0.4 },
  { type: "cover", dir: "r", dur: 0.4 },
  { type: "wipe", dir: "l", dur: 0.35 },
  { type: "uncover", dir: "d", dur: 0.4 },
  { type: "fade", dur: 0.5 },
  { type: "push", dir: "r", dur: 0.35 },
  { type: "cover", dir: "l", dur: 0.4 },
  { type: "dissolve", dur: 0.6 },
];

pres.slides.forEach((slide, idx) => {
  if (transitions[idx]) {
    slide.transition = transitions[idx];
  }
});

// ============================================================
// OUTPUT
// ============================================================
pres.writeFile({ fileName: "out/EasyApplyResume-项目介绍-v2.pptx" })
  .then(() => console.log("PPT v2 created successfully!"))
  .catch(err => console.error("Error:", err));
