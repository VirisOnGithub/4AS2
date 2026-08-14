#let conf(theme: blue, doctitle: none, subject: none, raw_nb_gutter: -1em, it) = {
  let default-theme = if (theme != none) { theme.rgb() } else { luma(200).rgb() }
  state("theme", default-theme).update(default-theme)

  // Edit built-in functions to use the theme color
  show heading: set text(fill: default-theme)

  show outline.entry.where(level: 1): set text(fill: default-theme)

  show footnote.entry: set text(fill: default-theme)

  show link: set text(fill: default-theme)

  show footnote: set text(fill: default-theme)

  show cite: set text(fill: default-theme)

  show raw.where(block: true): it => {
    if it.lang != "mermaid" {
      set par(justify: false)
      grid(
        columns: (100%, 100%),
        column-gutter: -100%,
        place(
          block(width: 100%, inset: 1em, for (i, line) in it.text.split("\n").enumerate() {
            box(width: 0pt, align(right, text(str(i + 1) + h(2em), fill: theme)))
            hide(line)
            linebreak()
          }),
          dx: -measure("0").width + raw_nb_gutter,
        ),
        block(radius: 1em, fill: luma(246), width: 100%, inset: 1em, it),
      )
    } else {
      it
    }
  }

  set document(title: doctitle)

  set page(footer: context align(right)[*#subject* #doctitle #h(1em) #counter(page).display("1")])

  show "=>": $=>$

  // = = souligne le texte
  show regex("(^| )=([^=]+)="): it => {
    let hasSpace = it.text.slice(0, 1) == " "
    if (hasSpace) {
      " " + underline(it.text.slice(2, -1))
    } else {
      underline(it.text.slice(1, -1))
    }
  }

  it
}

#let u(body) = underline(body)

#let tableofcontents = (
  outline(title: text("Table des matières", fill: black))
)

#let titlepage(logo: none, authors: none, toc: true, pb_before: true, pb_after: true, content: none) = {
  if (logo != none) {
    align(right, box(width: 80pt, logo))
    v(5em)
  }
  title()
  if (authors != none) {
    v(0.5em)
    box(width: 100%)[
      #if (type(authors) == str) {
        context text(size: 1.5em, authors, fill: state("theme").get())
      } else {
        for author in authors {
          context text(size: 1.5em, author, fill: state("theme").get())
          linebreak()
        }
      }
    ]
  }

  content

  if (logo != none and pb_before) {
    pagebreak()
  }

  if (toc) {
    tableofcontents
  }

  if (pb_after) {
    pagebreak()
  }
}

#let rounded-table(header, content) = align(center)[
  #context {
    let color = state("theme", luma(200)).get()
    block(radius: 5pt, stroke: color, clip: true, table(
      fill: (x, y) => if (calc.rem(y, 2) == 1) { color.lighten(80%) } else { white },
      columns: header.len(),
      stroke: (x, y) => (
        top: if y > 0 { color },
        left: if x > 0 { color },
      ),
      table.header(..header.map(h => text(weight: 700, [#h]))),
      ..content.flatten().map(c => [#c])
    ))
  }
]

#let rounded-image(image_file, caption: none, size: 100%, supplement: auto, kind: auto) = {
  context figure(
    caption: caption,
    gap: 1em,
    supplement: supplement,
    kind: kind,
  )[
    #rect(box(image_file, radius: 7pt, clip: true), stroke: state("theme", luma(200)).get(), radius: 10pt, width: size)
  ]
}

#let block-left(title: none, body, stroke-color: none) = (
  context pad(top: 1em)[
    #let color = if (stroke-color != none) { stroke-color } else { state("theme", luma(200)).get() }
    #let label = rect(stroke: color, radius: 50%, fill: white)[#text(title, fill: color)]
    #rect(stroke: color, radius: 5pt, width: 100%, {
      place(
        dy: -measure(label).height,
        dx: 5pt,
        label,
      )
      pad(body, 0.6em, top: 1em)
    })
  ]
)

#let block-right(title: none, body, stroke-color: none) = (
  context pad(top: 1em)[
    #let color = if (stroke-color != none) { stroke-color } else { state("theme", luma(200)).get() }
    #let label = rect(stroke: color, radius: 50%, fill: white)[#text(title, fill: color)]
    #rect(stroke: color, radius: 5pt, width: 100%, {
      place(
        dy: -measure(label).height,
        dx: 100% - 5pt - measure(label).width,
        label,
      )
      pad(body, 0.6em, top: 1em)
    })
  ]
)

#let block-full(title: none, body, stroke-color: none) = (
  context {
    let color = if (stroke-color != none) { stroke-color } else { state("theme", luma(200)).get() }
    block(stroke: color, radius: 5pt, width: 100%, {
      if (title != none) {
        place(text(title, fill: color), dx: 5pt, dy: measure(title).height / 2)
        pad(line(length: 100%, stroke: color), top: measure(title).height + 1em)
      }
      pad(body, 1em, top: if (title != none) { 0em } else { 0.6em })
    })
  }
)

#let c(content) = align(center)[#content]
