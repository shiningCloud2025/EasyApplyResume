async function copy2Clipboard(text) {
  if (typeof text !== 'string') return;

  if (window.isSecureContext && navigator.clipboard) {
    return await navigator.clipboard.writeText(text);
  } else {
    const textarea = document.createElement('textarea');
    let successed = false;
    textarea.value = text;
    textarea.style.position = 'fixed';
    textarea.style.opacity = 0;
    textarea.style.zIndex = '-10000';
    textarea.style.top = '-10000';
    document.body.appendChild(textarea);
    textarea.select();
    successed = document.execCommand('copy');
    document.body.removeChild(textarea);

    if (successed) {
      return;
    }

    throw new Error('Failed to copy content via "execCommand"!');
  }
}

export default copy2Clipboard;
