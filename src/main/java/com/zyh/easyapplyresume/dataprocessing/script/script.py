def convert_data_format(input_file, output_file):
    """
    转换txt文件中的数据格式
    输入格式：(50975,'石板镇',50952) 或 (50975,'石板镇',50952),（行尾带逗号）
    输出格式：A1(50975,"石板镇"), A2(...), ..., An(...)；（自动加逗号分隔，最后一条加分号，地名用双引号）
    """
    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            lines = f.readlines()

        # 存储所有行的状态（是否有效+内容），保持原文件顺序
        line_records = []
        valid_count = 0  # 有效行计数器（用于生成连续编号A1、A2...）

        # 第一步：遍历所有行，处理有效行并记录状态
        for line in lines:
            clean_line = line.strip()
            # 处理空行（保留原空行）
            if not clean_line:
                line_records.append(('empty', '\n'))
                continue

            # 去掉行尾可能存在的逗号（兼容输入格式）
            clean_line = clean_line.rstrip(',')

            # 处理有效格式行
            if clean_line.startswith('(') and clean_line.endswith(')'):
                content = clean_line[1:-1]
                parts = content.split(',')
                if len(parts) >= 2:
                    valid_count += 1  # 有效行编号递增
                    new_content = ','.join(parts[:2]).strip()
                    # 关键修改：将单引号替换为双引号
                    new_content = new_content.replace("'", '"')
                    # 核心格式：A+行号(内容)
                    valid_line_content = f"A{valid_count}({new_content})"
                    line_records.append(('valid', valid_line_content))
                else:
                    # 格式错误（字段不足2个）
                    line_records.append(('error', f"# 格式错误（字段不足）：{clean_line}\n"))
            else:
                # 格式错误（未用()包裹）
                line_records.append(('error', f"# 格式错误（未用()包裹）：{clean_line}\n"))

        # 第二步：生成最终输出内容（给有效行加逗号/分号）
        converted_lines = []
        current_valid_idx = 0  # 记录当前是第几个有效行

        for line_type, content in line_records:
            if line_type == 'valid':
                current_valid_idx += 1
                # 最后一个有效行加分号，其他加逗号
                if current_valid_idx == valid_count:
                    converted_lines.append(f"{content};\n")
                else:
                    converted_lines.append(f"{content},\n")
            elif line_type == 'empty':
                converted_lines.append(content)  # 保留空行
            elif line_type == 'error':
                converted_lines.append(content)  # 保留错误提示行

        # 写入输出文件
        with open(output_file, 'w', encoding='utf-8') as f:
            f.writelines(converted_lines)

        # 输出统计信息
        print(f"转换完成！")
        print(f"共处理 {len(lines)} 行数据，有效转换 {valid_count} 行，错误行 {sum(1 for t, _ in line_records if t == 'error')} 行")
        print(f"转换后的文件已保存为：{output_file}")

    except FileNotFoundError:
        print(f"错误：找不到文件 {input_file}")
    except Exception as e:
        print(f"错误：转换过程中出现异常 - {str(e)}")

if __name__ == "__main__":
    INPUT_FILE = "area.txt"        # 你的输入文件路径
    OUTPUT_FILE = "areaoutput.txt" # 输出文件路径
    convert_data_format(INPUT_FILE, OUTPUT_FILE)