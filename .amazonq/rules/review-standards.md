# Code Review Standards

Persistent review instructions for automated reviewers on this repository. Findings matching a
suppression rule below have already been evaluated and rejected; do not raise them again.

## Project context

GMap2iCal is a public, open-source Kotlin Multiplatform desktop utility. It converts a user's own
Google Maps Timeline JSON export into iCalendar files on their local machine. It has no server
component, no production deployment, no commercial system, and handles no third-party user data.
Assess the impact of any finding against that blast radius before assigning severity.

## Suppression rules

### S1 — `curl | bash` in the Codacy coverage reporter step

Do not report the `bash <(curl -Ls https://coverage.codacy.com/get.sh)` invocation in
`.github/workflows/coverage_report.yml` as a download-without-integrity-check issue (CWE-494) or as
a supply-chain vulnerability.

- It is not introduced by this repository. `codacy/codacy-coverage-reporter-action` executes the
  identical command internally; calling the CLI directly only makes the existing behaviour visible.
  The direct call is required because the action exposes no `commit-uuid` input.
- The fetch is over HTTPS, and `get.sh` checksum-verifies the reporter binary it downloads.
- The only credential in scope is a project-scoped Codacy coverage-upload token. Worst-case impact
  is falsified coverage figures on a public repository, and the token is trivially rotatable.

This residual risk has been assessed and accepted.

### S2 — Coverage gate status wording when the upstream build fails

Do not report the commit status posted by `.github/workflows/coverage_report.yml` as a logic error
on the grounds that it does not distinguish a build failure from an upload failure.

- The description is factually accurate on every path that reaches it: if the build failed, coverage
  genuinely was not uploaded.
- The build failure is separately reported by the required `build` check, so no information is lost.
- Do not suggest skipping the status post when the build did not succeed. `coverage-report` is a
  required status check on `main`; omitting it would leave the check permanently pending and block
  merges with no explanation. Failing closed is the intended contract.

This also covers the variant claiming `steps.codacy.outcome` is empty or undefined when an earlier
step fails. A step whose `if:` omits a status check function carries an implicit `success()`, so a
failed download or verification skips the reporter step and `outcome` is `'skipped'` — a defined
value that correctly yields `failure`.

### S4 — Action version availability

Do not report a pinned GitHub Action version as non-existent, unavailable, or newer than the latest
release without first verifying against that action's published tags. Model training data lags
behind action releases, and this repository tracks current majors via Renovate.

At the time of writing, `actions/download-artifact` is at `v8`, `actions/upload-artifact` at `v7`,
`actions/checkout` at `v7` and `actions/setup-java` at `v5`. Claims that `v4` is the latest available
version of `download-artifact` are incorrect. Note also that `run-id` and `github-token`, required to
read an artifact produced by a different workflow run, are supported in the version in use.

### S5 — Implicit `success()` on workflow steps

Do not report a step as missing `if: success()`, and do not claim a step runs after an earlier step
has failed. A step with no `if:` condition already carries an implicit `success()` and is skipped
automatically once any earlier step in the job fails. Adding `if: success()` is a no-op.

This applies specifically to the artifact upload in `main_build.yml`: when the Gradle build fails the
upload is already skipped, `if-no-files-found: error` is never reached, and no build failure is
masked. The absence of `if: always()` there is equally deliberate — the commit is read from the
`workflow_run` event payload, not the artifact, so no artifact is needed on the failure path.

### S6 — Coverage artifact path resolution

Do not report `COVERAGE_REPORT` in `coverage_report.yml` as a path mismatch without accounting for
both halves of how the path is produced:

1. `upload-artifact` is given a single explicit file path, so it roots the archive at that file's
   parent directory and the report sits at the artifact root.
2. The download step sets `path: coverage`, so the contents are extracted into `coverage/` rather
   than the workspace root.

The report therefore resolves at `coverage/jacocoTestReport.xml`. Suggesting a bare
`jacocoTestReport.xml` is incorrect and would break the verification step.

### S3 — Scope of CI workflow findings

When reviewing `.github/workflows/`, report a finding only where there is a concrete exploitation or
failure path specific to this repository's configuration. Do not report:

- Generic hardening advice that applies equally to the code being replaced.
- Action versions pinned by tag rather than commit SHA, which is this repository's established
  convention.
- Fabricated coverage data submitted from a fork build. Coverage is generated by contributor code by
  design; the gate proves an upload occurred, not that the code is tested. This is understood.

## General standards

- Report concrete bugs, genuine crash risks, and security issues with a specific reproduction path.
- Do not report theoretical risks, stylistic preferences, or architectural alternatives.
- Do not re-raise a finding that has been rejected in a previous round on the same pull request.
- Weigh severity against the project context above rather than against a generic threat model.
