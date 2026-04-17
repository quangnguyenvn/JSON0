# Patch Summary

## Da sua
- Parser: sua tach dau `:` trong key/value co quote, tranh parse sai khi key chua `:`.
- Parser: sua dem ngoac `{}` / `[]` de bo qua ky tu trong quoted string (ke ca escaped quote).
- Serializer: sua logic chen dau `,`, tranh sinh JSON loi khi field dau tien co value `null`.
- Parse rollback: khoi phuc day du state khi parse fail (`numItems`, `head`, `tail`, `isArray`, data length, keys map).
- Charset: dong bo ghi/parse theo charset runtime (khong hardcode ASCII cho key).
- Index map: sua `initMap` de rebuild dung tren shared storage, khong lam mat map cua parent khi parse nested JSON.
- CI workflow: sua lenh deploy Maven Central bi dinh chuoi thua.

## Test regression da them
- `parse_handlesColonInKey_andBracketInQuotedString`
- `toString_skipsNullWithoutLeadingComma`
- `parse_failure_rollsBackAllObservableState`

## Ket qua test
- `mvn test`: PASS (`3` tests, `0` failures, `0` errors).

## Commits
- `43d2739` Fix parser/serializer edge cases and add regression tests
- `8e3a2ea` Fix shared index-map initialization during parse
