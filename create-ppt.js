const pptxgen = require("pptxgenjs");

const pres = new pptxgen();
pres.layout = "LAYOUT_16x9";
pres.author = "shiningCloud2025";
pres.title = "EasyApplyResume - 易投简历智能平台";

// ============================================================
// Color Palette: Ocean Tech
// ============================================================
const C = {
  darkBg:    "0F172A",   // dark navy (title/end slides)
  primary:   "1E3A5F",   // deep blue
  secondary: "0EA5E9",   // sky blue (accent)
  teal:      "14B8A6",   // teal accent
  lightBg:   "F0F9FF",   // ice blue background
  white:     "FFFFFF",
  textDark:  "1E293B",   // dark text
  textMid:   "475569",   // medium gray text
  textLight: "94A3B8",   // light gray
  codeBg:    "0C4A6E",   // code background
  cardBg:    "FFFFFF",
  border:    "E2E8F0",
};

// Font settings
const FONT_H = "Trebuchet MS";  // headers
const FONT_B = "Calibri";        // body

// ============================================================
// Helper functions
// ============================================================
function makeShadow() {
  return { type: "outer", color: "000000", blur: 8, offset: 3, angle: 135, opacity: 0.10 };
}

function addFooter(slide, pageNum) {
  slide.addText(`EasyApplyResume | ${pageNum}`, {
    x: 0.5, y: 5.25, w: 9, h: 0.28,
    fontSize: 8, fontFace: FONT_B, color: C.textLight,
    align: "right", valign: "middle",
  });
}

// ============================================================
// SLIDE 1: 封面 - 项目介绍
// ============================================================
const s1 = pres.addSlide();
s1.background = { color: C.darkBg };

// Decorative shape - top right
s1.addShape(pres.shapes.RECTANGLE, {
  x: 6.5, y: 0, w: 3.5, h: 5.625,
  fill: { color: "1E3A5F", transparency: 45 },
});

// Decorative line accent
s1.addShape(pres.shapes.LINE, {
  x: 1.2, y: 2.55, w: 2.0, h: 0,
  line: { color: C.teal, width: 3 },
});

// Main title
s1.addText("EasyApplyResume", {
  x: 1.2, y: 1.2, w: 7.5, h: 1.0,
  fontSize: 44, fontFace: FONT_H, color: C.white,
  bold: true, margin: 0,
});

// Subtitle
s1.addText("易投简历 — AI 智能求职与简历管理平台", {
  x: 1.2, y: 2.2, w: 7.5, h: 0.5,
  fontSize: 20, fontFace: FONT_B, color: "7DD3FC",
  margin: 0,
});

// Tagline
s1.addText("Spring AI · ReAct Agent · RAG 知识库 · 多模型集成", {
  x: 1.2, y: 2.8, w: 7.5, h: 0.5,
  fontSize: 14, fontFace: FONT_B, color: C.textLight,
  margin: 0,
});

// Separator
s1.addShape(pres.shapes.LINE, {
  x: 1.2, y: 3.55, w: 3.0, h: 0,
  line: { color: C.secondary, width: 1.5 },
});

// Info section
s1.addText([
  { text: "技术架构：", options: { bold: true, color: C.white } },
  { text: "Spring Boot 3.3.5 + Java 21 + MySQL + PostgreSQL + Redis\n", options: { color: C.textLight, breakLine: true } },
  { text: "AI 能力：", options: { bold: true, color: C.white } },
  { text: "Spring AI + DashScope + 智谱AI + 豆包 + Ollama\n", options: { color: C.textLight, breakLine: true } },
  { text: "部署方式：", options: { bold: true, color: C.white } },
  { text: "Docker + Nacos + Spring Boot Admin + Prometheus", options: { color: C.textLight } },
], {
  x: 1.2, y: 3.8, w: 5.5, h: 1.5,
  fontSize: 12, fontFace: FONT_B, lineSpacingMultiple: 1.4, margin: 0,
});

// Right side decorative block
s1.addShape(pres.shapes.RECTANGLE, {
  x: 7.5, y: 1.0, w: 1.8, h: 0.06,
  fill: { color: C.teal },
});
s1.addShape(pres.shapes.RECTANGLE, {
  x: 7.5, y: 1.3, w: 1.8, h: 0.06,
  fill: { color: C.secondary },
});
s1.addShape(pres.shapes.RECTANGLE, {
  x: 7.5, y: 1.6, w: 1.8, h: 0.06,
  fill: { color: C.teal },
});

// Author info
s1.addText("作者：shiningCloud2025 | 2026年6月", {
  x: 1.2, y: 5.1, w: 4, h: 0.3,
  fontSize: 10, fontFace: FONT_B, color: "475569",
  margin: 0,
});

// ============================================================
// SLIDE 2: 问题背景
// ============================================================
const s2 = pres.addSlide();
s2.background = { color: C.lightBg };

// Left accent bar
s2.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 0.08, h: 5.625,
  fill: { color: C.primary },
});

// Title
s2.addText("问题背景", {
  x: 0.6, y: 0.3, w: 8, h: 0.6,
  fontSize: 32, fontFace: FONT_H, color: C.primary,
  bold: true, margin: 0,
});

// Subtitle
s2.addText("当前求职市场的核心痛点与AI破局之道", {
  x: 0.6, y: 0.85, w: 8, h: 0.4,
  fontSize: 13, fontFace: FONT_B, color: C.textMid,
  margin: 0,
});

// Pain points - 2x2 grid cards
const painPoints = [
  { title: "简历制作低效", desc: "求职者缺乏专业指导，简历内容空洞、结构混乱、表达不专业，严重影响面试邀约率", icon: "📝" },
  { title: "求职信息不对称", desc: "求职者难以获取全面的职位信息、行业动态和公司评价，缺乏有效的决策依据", icon: "🔍" },
  { title: "管理者运营负担重", desc: "HR和管理员面临大量重复性的简历筛选、用户管理、数据统计工作，效率低下", icon: "⚙️" },
  { title: "缺乏个性化服务", desc: "传统求职平台千篇一律，无法根据个人背景和目标岗位提供量身定制的优化建议", icon: "🎯" },
];

painPoints.forEach((pp, i) => {
  const col = i % 2;
  const row = Math.floor(i / 2);
  const x = 0.6 + col * 4.5;
  const y = 1.6 + row * 1.85;

  // Card background
  s2.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 4.2, h: 1.6,
    fill: { color: C.cardBg },
    shadow: makeShadow(),
  });
  // Left accent strip
  s2.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 0.06, h: 1.6,
    fill: { color: row === 0 ? C.secondary : C.teal },
  });

  s2.addText(pp.icon + "  " + pp.title, {
    x: x + 0.3, y: y + 0.15, w: 3.6, h: 0.4,
    fontSize: 15, fontFace: FONT_H, color: C.textDark,
    bold: true, margin: 0,
  });
  s2.addText(pp.desc, {
    x: x + 0.3, y: y + 0.6, w: 3.6, h: 0.85,
    fontSize: 11.5, fontFace: FONT_B, color: C.textMid,
    margin: 0, lineSpacingMultiple: 1.3,
  });
});

// Bottom note
s2.addShape(pres.shapes.RECTANGLE, {
  x: 0.6, y: 5.05, w: 8.8, h: 0.28,
  fill: { color: C.secondary, transparency: 85 },
});
s2.addText("核心思路：基于 Spring AI 生态，构建 AI Agent 驱动的智能求职助手，实现简历优化、智能问答、自动化运营", {
  x: 0.75, y: 5.05, w: 8.5, h: 0.28,
  fontSize: 11, fontFace: FONT_B, color: C.primary, italic: true,
  valign: "middle", margin: 0,
});

addFooter(s2, "2");

// ============================================================
// SLIDE 3: 技术选型
// ============================================================
const s3 = pres.addSlide();
s3.background = { color: C.lightBg };

s3.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 0.08, h: 5.625,
  fill: { color: C.primary },
});

s3.addText("技术选型", {
  x: 0.6, y: 0.3, w: 8, h: 0.6,
  fontSize: 32, fontFace: FONT_H, color: C.primary,
  bold: true, margin: 0,
});

s3.addText("Spring Boot 3.3.5 + Java 21 微服务架构，多层次 AI 能力集成", {
  x: 0.6, y: 0.85, w: 8, h: 0.4,
  fontSize: 13, fontFace: FONT_B, color: C.textMid,
  margin: 0,
});

// Tech stack categories - 3 columns
const techCats = [
  {
    title: "基础框架",
    color: C.secondary,
    items: [
      "Spring Boot 3.3.5 (Java 21)",
      "MyBatis-Plus 3.5.7",
      "Spring Security + JWT",
      "Nacos 配置/服务发现",
      "Docker 容器化部署",
    ],
  },
  {
    title: "AI & LLM",
    color: C.teal,
    items: [
      "Spring AI 1.0.0-M6",
      "通义千问 (DashScope)",
      "智谱AI (GLM-4)",
      "豆包 (OpenAI协议)",
      "Ollama 本地模型",
    ],
  },
  {
    title: "数据与存储",
    color: C.primary,
    items: [
      "MySQL 8.0 (主库)",
      "PostgreSQL + PgVector",
      "Redis (缓存/JWT)",
      "MinIO / 七牛云 OSS",
      "Spring Data Redis",
    ],
  },
];

techCats.forEach((cat, i) => {
  const x = 0.5 + i * 3.15;
  const y = 1.55;

  // Category header
  s3.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.9, h: 0.45,
    fill: { color: cat.color },
  });
  s3.addText(cat.title, {
    x: x, y: y, w: 2.9, h: 0.45,
    fontSize: 14, fontFace: FONT_H, color: C.white,
    bold: true, align: "center", valign: "middle", margin: 0,
  });

  // Items
  cat.items.forEach((item, j) => {
    s3.addShape(pres.shapes.RECTANGLE, {
      x: x, y: y + 0.55 + j * 0.52, w: 2.9, h: 0.45,
      fill: { color: C.cardBg },
      shadow: makeShadow(),
    });
    s3.addText(item, {
      x: x + 0.15, y: y + 0.55 + j * 0.52, w: 2.6, h: 0.45,
      fontSize: 11, fontFace: FONT_B, color: C.textDark,
      valign: "middle", margin: 0,
    });
  });
});

// Bottom: AI Architecture overview
s3.addShape(pres.shapes.RECTANGLE, {
  x: 0.5, y: 4.3, w: 9.0, h: 0.8,
  fill: { color: C.cardBg },
  shadow: makeShadow(),
});
s3.addShape(pres.shapes.RECTANGLE, {
  x: 0.5, y: 4.3, w: 0.06, h: 0.8,
  fill: { color: C.teal },
});

s3.addText([
  { text: "AI 能力概览：", options: { bold: true, color: C.textDark } },
  { text: "ReAct Agent · RAG 检索增强 · MCP 客户端 · 多云 LLM 适配 · 向量数据库 · 工具调用 (Function Calling) · 对话记忆持久化 · 敏感词过滤", options: { color: C.textMid } },
], {
  x: 0.75, y: 4.35, w: 8.5, h: 0.7,
  fontSize: 11.5, fontFace: FONT_B, margin: 0, valign: "middle",
});

addFooter(s3, "3");

// ============================================================
// SLIDE 4: AI 智能体架构
// ============================================================
const s4 = pres.addSlide();
s4.background = { color: C.white };

s4.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 0.08, h: 5.625,
  fill: { color: C.primary },
});

s4.addText("AI 智能体架构设计", {
  x: 0.6, y: 0.3, w: 8, h: 0.6,
  fontSize: 32, fontFace: FONT_H, color: C.primary,
  bold: true, margin: 0,
});

s4.addText("自研 ReAct Agent 模式：思考-行动循环 + 工具调用 + RAG 增强", {
  x: 0.6, y: 0.85, w: 8, h: 0.35,
  fontSize: 13, fontFace: FONT_B, color: C.textMid,
  margin: 0,
});

// Agent hierarchy - left side
s4.addShape(pres.shapes.RECTANGLE, {
  x: 0.6, y: 1.45, w: 4.6, h: 3.5,
  fill: { color: C.lightBg },
  shadow: makeShadow(),
});

s4.addText("Agent 继承体系", {
  x: 0.8, y: 1.55, w: 4.2, h: 0.35,
  fontSize: 14, fontFace: FONT_H, color: C.primary, bold: true, margin: 0,
});

// Hierarchy boxes
const agentLayers = [
  { label: "BaseAgent", desc: "状态管理 · 步骤循环 · 流式SSE · 资源清理", w: 3.2, color: C.primary },
  { label: "ReActAgent", desc: "Think & Act 模式 · 思考-行动循环 · 步骤编排", w: 3.6, color: "2563EB" },
  { label: "ToolCallAgent", desc: "工具调用管理 · 对话上下文维护 · 终止判断", w: 4.0, color: C.secondary },
];

agentLayers.forEach((layer, i) => {
  const y = 2.1 + i * 1.05;
  s4.addShape(pres.shapes.RECTANGLE, {
    x: 0.9 + (4.0 - layer.w) / 2, y: y, w: layer.w, h: 0.8,
    fill: { color: layer.color },
    shadow: makeShadow(),
  });
  s4.addText(layer.label, {
    x: 0.9 + (4.0 - layer.w) / 2, y: y + 0.05, w: layer.w, h: 0.35,
    fontSize: 13, fontFace: FONT_H, color: C.white,
    bold: true, align: "center", valign: "middle", margin: 0,
  });
  s4.addText(layer.desc, {
    x: 0.9 + (4.0 - layer.w) / 2, y: y + 0.4, w: layer.w, h: 0.3,
    fontSize: 9, fontFace: FONT_B, color: "CBD5E1",
    align: "center", valign: "middle", margin: 0,
  });

  // Arrows between layers (except last)
  if (i < agentLayers.length - 1) {
    s4.addShape(pres.shapes.LINE, {
      x: 2.9, y: y + 0.8, w: 0, h: 0.25,
      line: { color: C.textLight, width: 1.5, dashType: "dash" },
    });
  }
});

// Concrete agents
s4.addText("具体 Agent 实现", {
  x: 0.8, y: 0.85, w: 4.2, h: 0.35,
  fontSize: 14, fontFace: FONT_H, color: C.primary, bold: true, margin: 0, // this was overlapping with top, let me adjust
});

// Right side: components
s4.addShape(pres.shapes.RECTANGLE, {
  x: 5.5, y: 1.45, w: 4.0, h: 3.5,
  fill: { color: C.lightBg },
  shadow: makeShadow(),
});

s4.addText("核心组件", {
  x: 5.7, y: 1.55, w: 3.6, h: 0.35,
  fontSize: 14, fontFace: FONT_H, color: C.primary, bold: true, margin: 0,
});

const components = [
  { label: "ChatMemory", desc: "MySQL + 内存混合持久化，支持多轮对话上下文" },
  { label: "RAG Advisor", desc: "PgVector 向量库 + 云知识库 + 查询重写" },
  { label: "Tools (8个)", desc: "网页搜索、终端操作、文件管理、PDF生成、邮件等" },
  { label: "SensitiveWords", desc: "用户端/管理端双套敏感词过滤 Advisor" },
  { label: "MCP Client", desc: "Stdio/SSE 协议 MCP 工具集成" },
  { label: "LLM Router", desc: "DashScope / 智谱 / 豆包 / Ollama 多模型切换" },
];

components.forEach((comp, i) => {
  const y = 2.1 + i * 0.46;
  s4.addShape(pres.shapes.RECTANGLE, {
    x: 5.7, y: y, w: 3.6, h: 0.40,
    fill: { color: C.white },
    shadow: makeShadow(),
  });
  s4.addShape(pres.shapes.RECTANGLE, {
    x: 5.7, y: y, w: 0.06, h: 0.40,
    fill: { color: i % 2 === 0 ? C.secondary : C.teal },
  });
  s4.addText(comp.label, {
    x: 5.95, y: y + 0.02, w: 3.2, h: 0.18,
    fontSize: 11, fontFace: FONT_H, color: C.primary,
    bold: true, margin: 0,
  });
  s4.addText(comp.desc, {
    x: 5.95, y: y + 0.20, w: 3.2, h: 0.18,
    fontSize: 8.5, fontFace: FONT_B, color: C.textMid,
    margin: 0,
  });
});

// Bottom: concrete implementations summary
s4.addText([
  { text: "ResumeAssistantAgent", options: { bold: true, color: C.secondary } },
  { text: " — 简历AI助手", options: { color: C.textDark } },
], {
  x: 0.75, y: 4.95, w: 4.5, h: 0.2,
  fontSize: 10, fontFace: FONT_B, margin: 0,
});
s4.addText([
  { text: "SystemAssistantAgent", options: { bold: true, color: C.teal } },
  { text: " — 系统管理助手", options: { color: C.textDark } },
], {
  x: 5.7, y: 4.95, w: 4.5, h: 0.2,
  fontSize: 10, fontFace: FONT_B, margin: 0,
});

addFooter(s4, "4");

// ============================================================
// SLIDE 5: C端功能 - 简历AI助手
// ============================================================
const s5 = pres.addSlide();
s5.background = { color: C.lightBg };

s5.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 0.08, h: 5.625,
  fill: { color: C.primary },
});

s5.addText("核心功能：C端 — AI 简历助手", {
  x: 0.6, y: 0.3, w: 8, h: 0.6,
  fontSize: 32, fontFace: FONT_H, color: C.primary,
  bold: true, margin: 0,
});

s5.addText("面向求职者的全流程 AI 辅助：从简历优化到求职决策", {
  x: 0.6, y: 0.85, w: 8, h: 0.35,
  fontSize: 13, fontFace: FONT_B, color: C.textMid,
  margin: 0,
});

// Feature cards - 2x3 grid
const cFeatures = [
  { title: "智能简历润色", items: "语法优化 · 专业表达 · 结构重组 · 量化成果 · STAR法则改写" },
  { title: "定制化建议", items: "按行业/岗位定制 · 保研/留学场景适配 · 多版本输出 · 关键词优化" },
  { title: "RAG 知识增强", items: "PgVector 向量库 · 云知识库检索 · 查询重写优化 · 实时行业数据" },
  { title: "工具调用能力", items: "联网搜索职位 · 简历PDF生成 · 邮件发送 · 文件下载 · 数据抓取" },
  { title: "流式对话体验", items: "SSE 实时流式输出 · 多轮上下文记忆 · 混合持久化 · 敏感词过滤" },
  { title: "求职生态服务", items: "职位浏览/收藏 · 简历模板 · 面试题库 · 行业资讯 · 地区数据" },
];

cFeatures.forEach((feat, i) => {
  const col = i % 3;
  const row = Math.floor(i / 3);
  const x = 0.5 + col * 3.15;
  const y = 1.45 + row * 1.95;

  // Card
  s5.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.9, h: 1.75,
    fill: { color: C.cardBg },
    shadow: makeShadow(),
  });
  // Top accent
  s5.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.9, h: 0.06,
    fill: { color: row === 0 ? C.secondary : C.teal },
  });

  s5.addText(feat.title, {
    x: x + 0.2, y: y + 0.2, w: 2.5, h: 0.4,
    fontSize: 14, fontFace: FONT_H, color: C.primary,
    bold: true, margin: 0,
  });
  s5.addText(feat.items, {
    x: x + 0.2, y: y + 0.7, w: 2.5, h: 0.85,
    fontSize: 10.5, fontFace: FONT_B, color: C.textMid,
    margin: 0, lineSpacingMultiple: 1.35,
  });
});

addFooter(s5, "5");

// ============================================================
// SLIDE 6: B端功能 - 系统管理助手
// ============================================================
const s6 = pres.addSlide();
s6.background = { color: C.lightBg };

s6.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 0.08, h: 5.625,
  fill: { color: C.primary },
});

s6.addText("核心功能：B端 — AI 系统管理助手", {
  x: 0.6, y: 0.3, w: 8, h: 0.6,
  fontSize: 32, fontFace: FONT_H, color: C.primary,
  bold: true, margin: 0,
});

s6.addText("面向管理员的 AI 运维辅助：权限管理 · 内容运营 · 数据监控 · 智能诊断", {
  x: 0.6, y: 0.85, w: 8, h: 0.35,
  fontSize: 13, fontFace: FONT_B, color: C.textMid,
  margin: 0,
});

// Left: admin modules
const adminModules = [
  { title: "管理员管理", desc: "增删改查 · 角色权限分配 (RBAC) · 账户状态管理 · 登录日志" },
  { title: "内容运营", desc: "公告/广告管理 · FAQ问答库 · 项目/团队介绍 · 合作伙伴 · 媒体报道" },
  { title: "数据管理", desc: "省市区地图 · 行业分类 · 大学库 · 职位库 · 简历模板 · 求职攻略" },
  { title: "AI 运营工具", desc: "评分模型训练数据 · 模型版本管理 · LLM工具配置 · 邮件通信群发" },
  { title: "监控统计", desc: "日活统计 · 访问量统计 · 服务机器监控 · Prometheus指标体系" },
];

adminModules.forEach((mod, i) => {
  const y = 1.55 + i * 0.70;
  s6.addShape(pres.shapes.RECTANGLE, {
    x: 0.5, y: y, w: 5.2, h: 0.60,
    fill: { color: C.cardBg },
    shadow: makeShadow(),
  });
  s6.addShape(pres.shapes.RECTANGLE, {
    x: 0.5, y: y, w: 0.06, h: 0.60,
    fill: { color: C.secondary },
  });

  s6.addText(mod.title, {
    x: 0.8, y: y + 0.03, w: 4.7, h: 0.26,
    fontSize: 13, fontFace: FONT_H, color: C.primary, bold: true, margin: 0,
  });
  s6.addText(mod.desc, {
    x: 0.8, y: y + 0.30, w: 4.7, h: 0.26,
    fontSize: 10, fontFace: FONT_B, color: C.textMid, margin: 0,
  });
});

// Right: SystemManagerAssistant features
s6.addShape(pres.shapes.RECTANGLE, {
  x: 6.0, y: 1.55, w: 3.5, h: 3.5,
  fill: { color: C.cardBg },
  shadow: makeShadow(),
});
s6.addShape(pres.shapes.RECTANGLE, {
  x: 6.0, y: 1.55, w: 3.5, h: 0.5,
  fill: { color: C.primary },
});
s6.addText("AI 管理助手能力", {
  x: 6.0, y: 1.55, w: 3.5, h: 0.5,
  fontSize: 14, fontFace: FONT_H, color: C.white,
  bold: true, align: "center", valign: "middle", margin: 0,
});

const aiAdminFeatures = [
  "引导式故障诊断与排查",
  "系统配置与运维指导",
  "数据报表智能解读",
  "用户/简历问题快速定位",
  "MCP + RAG + 工具增强",
  "独立对话记忆管理",
  "管理端专属敏感词过滤",
  "Re2 重读增强理解",
];

aiAdminFeatures.forEach((feat, i) => {
  const y = 2.25 + i * 0.33;
  s6.addText("▸ " + feat, {
    x: 6.2, y: y, w: 3.1, h: 0.30,
    fontSize: 10.5, fontFace: FONT_B, color: C.textDark,
    valign: "middle", margin: 0,
  });
});

addFooter(s6, "6");

// ============================================================
// SLIDE 7: 代码实现展示
// ============================================================
const s7 = pres.addSlide();
s7.background = { color: C.darkBg };

s7.addText("代码实现", {
  x: 0.6, y: 0.2, w: 8, h: 0.55,
  fontSize: 32, fontFace: FONT_H, color: C.white,
  bold: true, margin: 0,
});

s7.addText("ReAct Agent 核心实现 · 工具调用 · 双安全过滤链", {
  x: 0.6, y: 0.7, w: 8, h: 0.3,
  fontSize: 13, fontFace: FONT_B, color: "7DD3FC",
  margin: 0,
});

// Code 1: ToolCallAgent - think() method (left column)
const code1 = `// ToolCallAgent.java — 思考阶段
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
      .chatResponse();`;

s7.addShape(pres.shapes.RECTANGLE, {
  x: 0.4, y: 1.1, w: 4.55, h: 3.8,
  fill: { color: "0C1B33" },
});

s7.addText("ToolCallAgent.think()", {
  x: 0.55, y: 1.15, w: 4.2, h: 0.3,
  fontSize: 10, fontFace: "Consolas", color: C.teal,
  bold: true, margin: 0,
});

s7.addText(code1, {
  x: 0.55, y: 1.45, w: 4.2, h: 3.5,
  fontSize: 8.5, fontFace: "Consolas", color: "CBD5E1",
  margin: 0, lineSpacingMultiple: 1.25,
});

// Code 2: Security Config (right column)
const code2 = `// Security双链: UserSecurityConfig & AdminSecurityConfig
@Configuration
@EnableWebSecurity
@Order(1)  // User先匹配
public class UserSecurityConfig {
    @Bean
    public SecurityFilterChain userSecurity...(HttpSecurity http){
        http
          .securityMatcher("/user/**")
          .cors(cors->cors.configurationSource(corsConf))
          .csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(session->session
            .sessionCreationPolicy(SessionCreationPolicy
                                       .STATELESS))
          .authorizeHttpRequests(auth->auth
            .requestMatchers("/user/auth/**").permitAll()
            .anyRequest().authenticated())
          .exceptionHandling(ex->ex
            .authenticationEntryPoint(...))
          .addFilterBefore(userJwtAuthFilter,
            UsernamePasswordAuthFilter.class);`;

s7.addShape(pres.shapes.RECTANGLE, {
  x: 5.05, y: 1.1, w: 4.55, h: 3.8,
  fill: { color: "0C1B33" },
});

s7.addText("双 SecurityFilterChain 配置", {
  x: 5.2, y: 1.15, w: 4.2, h: 0.3,
  fontSize: 10, fontFace: "Consolas", color: C.teal,
  bold: true, margin: 0,
});

s7.addText(code2, {
  x: 5.2, y: 1.45, w: 4.2, h: 3.5,
  fontSize: 8.5, fontFace: "Consolas", color: "CBD5E1",
  margin: 0, lineSpacingMultiple: 1.25,
});

// Bottom note
s7.addShape(pres.shapes.RECTANGLE, {
  x: 0.4, y: 5.05, w: 9.2, h: 0.25,
  fill: { color: C.teal, transparency: 75 },
});
s7.addText("以上代码片段均来自项目真实源码：ToolCallAgent.java (think方法) 与 UserSecurityConfig.java (双SecurityFilterChain)", {
  x: 0.55, y: 5.05, w: 8.9, h: 0.25,
  fontSize: 9, fontFace: FONT_B, color: "CBD5E1", italic: true,
  margin: 0, valign: "middle",
});

addFooter(s7, "7");

// ============================================================
// SLIDE 8: 未来前景
// ============================================================
const s8 = pres.addSlide();
s8.background = { color: C.lightBg };

s8.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 0.08, h: 5.625,
  fill: { color: C.primary },
});

s8.addText("未来前景", {
  x: 0.6, y: 0.3, w: 8, h: 0.6,
  fontSize: 32, fontFace: FONT_H, color: C.primary,
  bold: true, margin: 0,
});

s8.addText("持续迭代 · 拥抱 AI 前沿 · 打造全链路智能求职生态", {
  x: 0.6, y: 0.85, w: 8, h: 0.35,
  fontSize: 13, fontFace: FONT_B, color: C.textMid,
  margin: 0,
});

// Timeline style future plans
const futurePlans = [
  {
    phase: "近期 (1-3月)",
    color: C.secondary,
    items: [
      "前端界面开发完成",
      "多租户 SaaS 化改造",
      "接入更多 LLM (DeepSeek等)",
      "简历评分模型上线 (XGBoost)",
    ],
  },
  {
    phase: "中期 (3-6月)",
    color: C.teal,
    items: [
      "微服务化拆分 (Spring Cloud)",
      "全链路可观测性增强",
      "智能岗位匹配推荐",
      "移动端适配 (小程序)",
    ],
  },
  {
    phase: "远期 (6-12月)",
    color: C.primary,
    items: [
      "多 Agent 协作架构 (多智能体)",
      "AI 模拟面试与反馈",
      "企业端 AI 筛选助手",
      "构建求职知识图谱",
    ],
  },
];

futurePlans.forEach((plan, i) => {
  const x = 0.4 + i * 3.2;
  const y = 1.5;

  // Phase header
  s8.addShape(pres.shapes.RECTANGLE, {
    x: x, y: y, w: 2.95, h: 0.5,
    fill: { color: plan.color },
  });
  s8.addText(plan.phase, {
    x: x, y: y, w: 2.95, h: 0.5,
    fontSize: 14, fontFace: FONT_H, color: C.white,
    bold: true, align: "center", valign: "middle", margin: 0,
  });

  // Items
  plan.items.forEach((item, j) => {
    s8.addShape(pres.shapes.RECTANGLE, {
      x: x, y: y + 0.6 + j * 0.65, w: 2.95, h: 0.55,
      fill: { color: C.cardBg },
      shadow: makeShadow(),
    });
    s8.addShape(pres.shapes.RECTANGLE, {
      x: x, y: y + 0.6 + j * 0.65, w: 0.05, h: 0.55,
      fill: { color: plan.color },
    });
    s8.addText(item, {
      x: x + 0.2, y: y + 0.6 + j * 0.65, w: 2.55, h: 0.55,
      fontSize: 11.5, fontFace: FONT_B, color: C.textDark,
      valign: "middle", margin: 0,
    });
  });
});

// Vision statement
s8.addShape(pres.shapes.RECTANGLE, {
  x: 0.4, y: 4.5, w: 9.2, h: 0.65,
  fill: { color: C.primary },
});
s8.addText("愿景：成为求职者最信赖的 AI 求职伙伴，用人均 AI 赋能每位求职者找到理想工作", {
  x: 0.6, y: 4.5, w: 8.8, h: 0.65,
  fontSize: 16, fontFace: FONT_H, color: C.white,
  align: "center", valign: "middle", margin: 0,
  bold: true,
});

addFooter(s8, "8");

// ============================================================
// SLIDE 9: 致谢
// ============================================================
const s9 = pres.addSlide();
s9.background = { color: C.darkBg };

// Decorative
s9.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 0, w: 10, h: 0.1,
  fill: { color: C.teal },
});

s9.addText("致  谢", {
  x: 1, y: 1.2, w: 8, h: 1.0,
  fontSize: 48, fontFace: FONT_H, color: C.white,
  bold: true, align: "center", valign: "middle", margin: 0,
});

// Separator
s9.addShape(pres.shapes.LINE, {
  x: 4.0, y: 2.3, w: 2.0, h: 0,
  line: { color: C.teal, width: 3 },
});

// Thank you message
s9.addText([
  { text: "感谢各位老师和同学对 EasyApplyResume 项目的关注与支持\n\n", options: { breakLine: true, color: "CBD5E1" } },
  { text: "本项目基于 Spring AI 生态，探索了 AI Agent 在求职领域的应用\n", options: { breakLine: true, color: "94A3B8" } },
  { text: "从自研 ReAct Agent 到 RAG 增强检索，从多模型适配到工具集成\n", options: { breakLine: true, color: "94A3B8" } },
  { text: "这是一次将前沿 AI 技术与实际业务场景结合的有益尝试\n\n", options: { breakLine: true, color: "94A3B8" } },
  { text: "技术栈：", options: { bold: true, color: C.teal } },
  { text: "Spring Boot 3.3.5 · Spring AI · MyBatis-Plus · PostgreSQL/PgVector · Redis · Nacos · Docker\n", options: { breakLine: true, color: "94A3B8" } },
  { text: "AI模型：", options: { bold: true, color: C.teal } },
  { text: "通义千问 (DashScope) · 智谱AI (GLM-4) · 豆包 (OpenAI) · Ollama\n\n", options: { breakLine: true, color: "94A3B8" } },
  { text: "联系方式：shining_cloud2025@163.com\n", options: { breakLine: true, color: "7DD3FC" } },
  { text: "GitHub: github.com/shiningCloud2025", options: { color: "7DD3FC" } },
], {
  x: 1.5, y: 2.7, w: 7, h: 2.6,
  fontSize: 13, fontFace: FONT_B,
  align: "center", valign: "top", margin: 0,
  lineSpacingMultiple: 1.3,
});

// Bottom bar
s9.addShape(pres.shapes.RECTANGLE, {
  x: 0, y: 5.525, w: 10, h: 0.1,
  fill: { color: C.teal },
});

// ============================================================
// OUTPUT
// ============================================================
pres.writeFile({ fileName: "out/EasyApplyResume-项目介绍.pptx" })
  .then(() => console.log("PPT created successfully!"))
  .catch(err => console.error("Error:", err));
