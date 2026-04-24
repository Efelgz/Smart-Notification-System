const state = {
  dashboard: null,
};

const statusLine = document.getElementById("status-line");
const notificationForm = document.getElementById("notification-form");
const muteForm = document.getElementById("mute-form");
const refreshBtn = document.getElementById("refresh-btn");

notificationForm.addEventListener("submit", onSubmitNotification);
muteForm.addEventListener("submit", onMuteApp);
refreshBtn.addEventListener("click", () => loadDashboard(true));

document.addEventListener("click", async (event) => {
  const feedbackButton = event.target.closest("button[data-feedback-id]");
  if (feedbackButton) {
    const notificationId = Number(feedbackButton.dataset.feedbackId);
    const isImportant = feedbackButton.dataset.feedbackValue === "1";
    await submitFeedback(notificationId, isImportant);
  }

  const unmuteButton = event.target.closest("button[data-unmute]");
  if (unmuteButton) {
    await unmuteApp(unmuteButton.dataset.unmute);
  }
});

loadDashboard(false);
setInterval(() => {
  loadDashboard(false).catch(() => {
    setStatus("Auto-refresh failed. Retrying...");
  });
}, 10000);

async function loadDashboard(withStatus = true) {
  const response = await fetch("/api/dashboard");
  if (!response.ok) {
    throw new Error("Failed to load dashboard");
  }
  const payload = await response.json();
  state.dashboard = payload;
  renderDashboard(payload);
  if (withStatus) {
    setStatus("Dashboard refreshed.");
  }
}

function renderDashboard(payload) {
  const stats = payload.stats || {};
  document.getElementById("stat-total").textContent = stats.total_notifications ?? 0;
  document.getElementById("stat-critical").textContent = stats.critical_notifications ?? 0;
  document.getElementById("stat-feedback").textContent = stats.feedback_count ?? 0;

  renderNotifications("critical-list", payload.critical_feed || []);
  renderNotifications("all-list", payload.latest_notifications || []);
  renderMutedApps(payload.muted_apps || []);
  renderKeywords(payload.keyword_weights || []);
}

function renderNotifications(containerId, items) {
  const container = document.getElementById(containerId);
  container.innerHTML = "";
  if (!items.length) {
    container.innerHTML = '<p class="empty">No notifications yet.</p>';
    return;
  }

  const tpl = document.getElementById("notification-template");

  for (const item of items) {
    const clone = tpl.content.cloneNode(true);
    const root = clone.querySelector(".notif-item");
    const source = clone.querySelector(".notif-source");
    const badge = clone.querySelector(".badge");
    const content = clone.querySelector(".notif-content");
    const sender = clone.querySelector(".notif-sender");
    const time = clone.querySelector(".notif-time");
    const importantBtn = clone.querySelector(".important");
    const neutralBtn = clone.querySelector(".neutral");

    source.textContent = `${item.source}${item.platform ? ` • ${item.platform}` : ""}`;
    badge.textContent = item.is_critical ? "Critical" : "Noise";
    badge.classList.add(item.is_critical ? "badge-critical" : "badge-noise");
    content.textContent = item.content;
    sender.textContent = `From: ${item.sender}`;
    time.textContent = new Date(item.received_at).toLocaleString();

    if (item.feedback === null) {
      importantBtn.dataset.feedbackId = String(item.id);
      importantBtn.dataset.feedbackValue = "1";
      neutralBtn.dataset.feedbackId = String(item.id);
      neutralBtn.dataset.feedbackValue = "0";
    } else {
      importantBtn.disabled = true;
      neutralBtn.disabled = true;
      const verdict = document.createElement("small");
      verdict.textContent = `Feedback: ${item.feedback ? "Important" : "Noise"}`;
      verdict.style.color = "#4d5861";
      root.querySelector(".feedback-row").appendChild(verdict);
    }

    container.appendChild(clone);
  }
}

function renderMutedApps(items) {
  const container = document.getElementById("muted-apps");
  container.innerHTML = "";
  if (!items.length) {
    container.innerHTML = '<p class="empty">No muted app.</p>';
    return;
  }

  for (const appName of items) {
    const chip = document.createElement("span");
    chip.className = "chip";
    chip.textContent = appName;

    const button = document.createElement("button");
    button.type = "button";
    button.textContent = "unmute";
    button.dataset.unmute = appName;

    chip.appendChild(button);
    container.appendChild(chip);
  }
}

function renderKeywords(items) {
  const container = document.getElementById("keyword-weights");
  container.innerHTML = "";
  if (!items.length) {
    container.innerHTML = '<p class="empty">No keyword weights yet.</p>';
    return;
  }

  for (const entry of items) {
    const chip = document.createElement("span");
    chip.className = "chip";
    chip.textContent = `${entry.keyword}: ${Number(entry.weight).toFixed(2)}`;
    container.appendChild(chip);
  }
}

async function onSubmitNotification(event) {
  event.preventDefault();
  const form = event.currentTarget;
  const payload = {
    sender: form.sender.value.trim(),
    source: form.source.value.trim(),
    platform: form.platform.value.trim() || null,
    subject: form.subject.value.trim() || null,
    content: form.content.value.trim(),
  };

  try {
    const response = await fetch("/api/notifications", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.detail || "Failed to process notification");
    }

    form.reset();
    await loadDashboard(false);
    if (data.stored === false) {
      setStatus(`Skipped: ${data.reason}`);
    } else {
      setStatus(`Stored #${data.id} with score ${data.score}`);
    }
  } catch (error) {
    setStatus(error.message);
  }
}

async function onMuteApp(event) {
  event.preventDefault();
  const appName = event.currentTarget.app_name.value.trim();
  if (!appName) {
    return;
  }

  try {
    const response = await fetch("/api/muted-apps", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ app_name: appName }),
    });
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.detail || "Failed to mute app");
    }

    event.currentTarget.reset();
    renderMutedApps(data.muted_apps || []);
    setStatus(`${appName} muted.`);
  } catch (error) {
    setStatus(error.message);
  }
}

async function unmuteApp(appName) {
  try {
    const response = await fetch(`/api/muted-apps/${encodeURIComponent(appName)}`, {
      method: "DELETE",
    });
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.detail || "Failed to unmute app");
    }

    renderMutedApps(data.muted_apps || []);
    setStatus(`${appName} unmuted.`);
  } catch (error) {
    setStatus(error.message);
  }
}

async function submitFeedback(notificationId, isImportant) {
  try {
    const response = await fetch("/api/feedback", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        notification_id: notificationId,
        is_important: isImportant,
      }),
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.detail || "Failed to save feedback");
    }

    await loadDashboard(false);
    const keywordCount = (data.adjusted_keywords || []).length;
    setStatus(
      `Feedback saved for #${notificationId}. Model updated with ${keywordCount} tokens.`
    );
  } catch (error) {
    setStatus(error.message);
  }
}

function setStatus(message) {
  statusLine.textContent = message;
}
