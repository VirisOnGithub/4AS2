// Imports
#import "@preview/fontawesome:0.6.0": fa-use-pro
#fa-use-pro()
#import "@preview/brilliant-cv:3.3.0": cv
#let metadata = toml("./metadata.toml")
#let cv-language = sys.inputs.at("language", default: none)
#let metadata = if cv-language != none {
  metadata + (language: cv-language)
} else {
  metadata
}

#if cv-language == "fr" {
  metadata.layout.header.display_profile_photo = true
  metadata.layout.header.header_align = "left"
} else {
  metadata.layout.header.display_profile_photo = false
  metadata.layout.header.header_align = "center"
}

#let import-modules(modules, lang: metadata.language) = {
  for module in modules {
    include {
      "modules_" + lang + "/" + module + ".typ"
    }
  }
}


#show: cv.with(
  metadata,
  // profile-photo: image("assets/portrait.jpg"),
  profile-photo: "",
  // To use custom image icons in personal.info.custom-N entries,
  // pass them here (keys must match the custom-N keys in metadata.toml):
  // custom-icons: (
  //   "custom-1": image("assets/my-icon.png"),
  // ),
)

#set text(size: 12pt)

#import-modules((
  "education",
  "professional",
  "projects",
  // "certificates",
  // "publications",
  "skills",
))
