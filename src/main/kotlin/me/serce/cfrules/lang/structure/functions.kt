package me.serce.cfrules.lang.structure

data class CfFunction(val name: String, val description: String = "")

val allCfFunctions = listOf(
  // Transformation functions
  "any",
  "all",
  "concat",
  "ends_with",
  "len",
  "lookup_json_string",
  "lower",
  "regex_replace",
  "remove_bytes",
  "starts_with",
  "to_string",
  "upper",
  "url_decode",
  "uuidv4",

  // Magic Firewall Functions
  "bit_slice",

  // HMAC validation
  "is_timed_hmac_valid_v0",
  
).map {
  CfFunction(it)
}

