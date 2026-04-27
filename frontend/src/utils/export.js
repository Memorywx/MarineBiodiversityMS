export const exportCSV = (filename, headers, rows) => {
  const headerLine = headers.map(h => h.label).join(',')
  const dataLines = rows.map(row =>
    headers.map(h => {
      let val = row[h.prop]
      if (val === null || val === undefined) val = ''
      val = String(val).replace(/"/g, '""')
      if (val.includes(',') || val.includes('\n') || val.includes('"')) {
        val = `"${val}"`
      }
      return val
    }).join(',')
  )
  const csv = [headerLine, ...dataLines].join('\n')
  const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = filename
  link.click()
  URL.revokeObjectURL(link.href)
}
