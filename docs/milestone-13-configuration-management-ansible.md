# Milestone 13: Configuration Management Script
## Automated Microinsurance Enrollment Platform

---

### 1. Server Prerequisites & Configuration Specification
To deploy and operate the Microinsurance Enrollment Platform consistently across bare-metal or cloud VMs, the following node specifications are required:

| Resource Type | Resource Identifier | Specific State / Value | Rationale |
| :--- | :--- | :--- | :--- |
| **Packages** | `openjdk-21-jre-headless` | Installed (`apt: state=present`) | JRE 21 runtime required for Spring Boot 3.2 execution. |
| **Packages** | `curl`, `ufw`, `git` | Installed (`state=present`) | Utilities for health checks, firewall, and versioning. |
| **System Group** | `microinsurance` | System group created | Non-root isolation policy. |
| **System User** | `microinsurance` | System user, `/bin/false` shell | Least privilege compliance; prevents interactive shell access. |
| **Directories** | `/opt/microinsurance/bin` | Owned by `microinsurance:microinsurance`, `0750` | Houses executable JAR binary. |
| **Directories** | `/opt/microinsurance/config` | Owned by `microinsurance:microinsurance`, `0750` | Houses environment properties. |
| **Directories** | `/opt/microinsurance/logs` | Owned by `microinsurance:microinsurance`, `0750` | Houses application audit and transaction logs. |
| **Directories** | `/opt/microinsurance/backups`| Owned by `microinsurance:microinsurance`, `0750` | Holds prior stable binary for instant rollback. |
| **Files** | `/etc/systemd/system/microinsurance.service` | Mode `0644` | Systemd service unit managing lifecycle and auto-restart. |
| **Ports** | `8080/tcp` | Allowed in UFW firewall | Ingress traffic for web portal and REST endpoints. |
| **Services** | `microinsurance` | `state=started`, `enabled=yes` | Boot-persistent daemon service. |

---

### 2. Ansible Playbook Structure
The project configuration repository contains:
```
ansible/
├── inventory.ini             # Target hosts and host variables
├── playbook.yml              # Master orchestration playbook
├── rollback.yml              # Emergency rollback playbook
└── roles/
    └── microinsurance/
        └── tasks/
            └── main.yml      # Idempotent tasks
```

---

### 3. First Execution Log
```bash
$ ansible-playbook -i ansible/inventory.ini ansible/playbook.yml

PLAY [Provision and Deploy Microinsurance Enrollment Platform] *****************

TASK [Gathering Facts] *********************************************************
ok: [target-server-01]

TASK [microinsurance : Update apt cache and install prerequisite system packages] ***
changed: [target-server-01]

TASK [microinsurance : Create dedicated system group for microinsurance] *******
changed: [target-server-01]

TASK [microinsurance : Create dedicated system user for microinsurance] ********
changed: [target-server-01]

TASK [microinsurance : Create required application directory tree] *************
changed: [target-server-01] => (item=/opt/microinsurance)
changed: [target-server-01] => (item=/opt/microinsurance/bin)
changed: [target-server-01] => (item=/opt/microinsurance/config)
changed: [target-server-01] => (item=/opt/microinsurance/logs)
changed: [target-server-01] => (item=/opt/microinsurance/backups)

TASK [microinsurance : Copy new application release JAR to target node] ********
changed: [target-server-01]

TASK [microinsurance : Deploy production configuration properties] *************
changed: [target-server-01]

TASK [microinsurance : Configure and deploy systemd unit service file] *********
changed: [target-server-01]

TASK [microinsurance : Allow incoming traffic on application port via UFW firewall] *
changed: [target-server-01]

RUNNING HANDLER [microinsurance : Reload systemd daemon] ***********************
changed: [target-server-01]

RUNNING HANDLER [microinsurance : Restart microinsurance service] ***************
changed: [target-server-01]

TASK [microinsurance : Ensure microinsurance service is enabled and actively running] *
ok: [target-server-01]

TASK [microinsurance : Validate service health via HTTP probe] *****************
ok: [target-server-01]

TASK [microinsurance : Display health verification payload] ********************
ok: [target-server-01] => {
    "msg": "Health check passed successfully: {'status': 'UP', 'service': 'Microinsurance Enrollment Platform'}"
}

PLAY RECAP *********************************************************************
target-server-01            : ok=14   changed=10   unreachable=0    failed=0    skipped=0    rescued=0    ignored=0
```
