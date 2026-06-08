"""合并：temp-new(10页)为基础，把架构图页替换为v27中用户手动插入的版本"""
import zipfile, io, shutil

base = "out/temp-new.pptx"
v27 = "out/EasyApplyResume-v27.pptx"
out = "out/EasyApplyResume-v47.pptx"

# temp-new slide5(架构图) ← v27 slide4(用户已手动插架构图)
replace_map = {"ppt/slides/slide5.xml": "ppt/slides/slide4.xml"}

shutil.copy2(base, out)

tmp = io.BytesIO()
with zipfile.ZipFile(out, 'r') as z_out:
    with zipfile.ZipFile(tmp, 'w', zipfile.ZIP_DEFLATED) as z_tmp:
        with zipfile.ZipFile(v27, 'r') as z_old:
            for fname in z_out.namelist():
                if fname in replace_map:
                    data = z_old.read(replace_map[fname])
                    print(f"Replaced: {fname} ← v27/{replace_map[fname]}")
                else:
                    data = z_out.read(fname)
                z_tmp.writestr(fname, data)

with open(out, 'wb') as f:
    f.write(tmp.getvalue())

print(f"\nSaved: {out}")
print("10-page PPT with TOC as page 2, architecture image from v27 on page 5")
