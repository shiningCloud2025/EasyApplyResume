from lxml import etree; import zipfile
P='http://schemas.openxmlformats.org/presentationml/2006/main'
with zipfile.ZipFile('out/EasyApplyResume-v16.pptx','r') as z:
    s2=etree.fromstring(z.read('ppt/slides/slide2.xml'))
    tm=s2.find(f'{{{P}}}timing')
    ces=tm.findall(f'.//{{{P}}}cTn[@nodeType="clickEffect"]')
    aes=tm.findall(f'.//{{{P}}}cTn[@nodeType="afterEffect"]')
    print(f'slide2: clickEffect={len(ces)}, afterEffect={len(aes)}')
    for ae in aes[:2]:
        pc=ae.get('presetClass');pi=ae.get('presetID');ps=ae.get('presetSubtype')
        sc=ae.find(f'{{{P}}}stCondLst/{{{P}}}cond')
        delay=sc.get('delay') if sc is not None else 'N/A'
        print(f'  presetClass={pc}, presetID={pi}, presetSubtype={ps}, delay={delay}')
