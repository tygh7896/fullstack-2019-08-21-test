package com.sbs.cuni.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.cuni.dto.Article;
import com.sbs.cuni.dto.ArticleReply;
import com.sbs.cuni.dto.Board;
import com.sbs.cuni.dto.Member;
import com.sbs.cuni.service.ArticleService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ArticleController {
	@Autowired
	private ArticleService articleService;

	// 게시물 리스팅
	@RequestMapping("article/list")
	public String showList(Model model, @RequestParam Map<String, Object> param, long boardId) {
		Board board = articleService.getBoard(boardId);

		model.addAttribute("board", board);

		// 게시물 가져올 때 댓글 개수도 가져오도록
		param.put("extra__repliesCount", true);

		if (param.containsKey("page") == false) {
			param.put("page", "1");
		}

		Map<String, Object> pagedListRs = articleService.getPagedList(param);

		model.addAttribute("pagedListRs", pagedListRs);

		return "article/list";
	}

	// 게시물 상세페이지
	@RequestMapping("article/detail")
	public String showDetail(@RequestParam(value = "id", defaultValue = "0") long id, Model model, long boardId) {
		Board board = articleService.getBoard(boardId);
		
		articleService.hitUp(id);

		model.addAttribute("board", board);

		if (id == 0) {
			model.addAttribute("alertMsg", "id를 정확히 입력해주세요.");
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		Article article = articleService.getOne(Maps.of("id", id));

		model.addAttribute("article", article);

		return "article/detail";
	}

	@RequestMapping("/article/getReplies")
	@ResponseBody
	public Map<String, Object> getAllMessages(int articleId, int from) {
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("replies", articleService.getReplies(Maps.of("articleId", articleId, "from", from)));

		return rs;
	}

	@RequestMapping("/article/add")
	public String showAdd(long boardId, Model model, HttpServletRequest request) {
		Board board = articleService.getBoard(boardId);

		model.addAttribute("board", board);

		Member loginedMember = (Member) request.getAttribute("loginedMember");

		if (boardId == 1 && loginedMember.getPermissionLevel() != 1) {
			model.addAttribute("alertMsg", "권한이 없습니다.");
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		return "article/add";
	}

	@RequestMapping("/article/doAdd")
	public String doAdd(Model model, @RequestParam Map<String, Object> param, HttpSession session, long boardId,
			HttpServletRequest request) {
		param.put("memberId", session.getAttribute("loginedMemberId"));
		long newId = articleService.add(param);

		String msg = newId + "번 게시물이 추가되었습니다.";
		String redirectUrl = "/article/detail?id=" + newId + "&boardId=" + boardId;

		model.addAttribute("alertMsg", msg);
		model.addAttribute("redirectUrl", redirectUrl);

		return "common/redirect";
	}

	@RequestMapping("/article/modify")
	public String showModify(@RequestParam(value = "id", defaultValue = "0") int id, long boardId, Model model) {

		Board board = articleService.getBoard(boardId);

		model.addAttribute("board", board);

		if (id == 0) {
			model.addAttribute("alertMsg", "id를 정확히 입력해주세요.");
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		Article article = articleService.getOne(Maps.of("id", id));

		model.addAttribute("article", article);

		return "article/modify";
	}

	@RequestMapping("/article/doModify")
	public String doModify(Model model, @RequestParam Map<String, Object> param, HttpSession session, long id,
			long boardId, HttpServletRequest req) {
		// String referer = req.getHeader("referer");
		long loginedMemberId = (long) session.getAttribute("loginedMemberId");
		Map<String, Object> checkModifyPermmisionRs = articleService.checkModifyPermmision(id, loginedMemberId);

		if (((String) checkModifyPermmisionRs.get("resultCode")).startsWith("F-")) {
			model.addAttribute("alertMsg", ((String) checkModifyPermmisionRs.get("msg")));
			model.addAttribute("historyBack", true);

			return "common/redirect";
		}

		Map<String, Object> modifyRs = articleService.modify(param);

		String msg = (String) modifyRs.get("msg");
		String resultCode = (String) modifyRs.get("resultCode");

		if (resultCode.startsWith("S-")) {
			String redirectUrl = "/article/detail?id=" + id + "&boardId=" + boardId;
			model.addAttribute("redirectUrl", redirectUrl);
		} else {
			model.addAttribute("historyBack", true);
		}

		model.addAttribute("alertMsg", msg);

		return "common/redirect";
	}

	@RequestMapping("/article/doDelete")
	public String doDelete(Model model, @RequestParam Map<String, Object> param, HttpServletRequest request, long id,
			long boardId) {
		param.put("id", id);
		// 관리자인지 체크
		// 작성자인지 체크

		boolean hasAPermmision = true;

		Member loginedMember = (Member) request.getAttribute("loginedMember");

		Article article = articleService.getOne(Maps.of("id", id));
		boolean isWriter = article.getMemberId() == loginedMember.getId();

		if (loginedMember.getPermissionLevel() == 0 && isWriter == false) {
			hasAPermmision = false;
		}

		if (hasAPermmision == false) {
			model.addAttribute("historyBack", true);
			model.addAttribute("alertMsg", "권한이 없습니다.");

			return "common/redirect";
		}

		Map<String, Object> deleteRs = articleService.delete(param);

		String msg = (String) deleteRs.get("msg");
		String resultCode = (String) deleteRs.get("resultCode");

		if (resultCode.startsWith("S-")) {
			String redirectUrl = "/article/list?boardId=" + boardId;
			model.addAttribute("redirectUrl", redirectUrl);
		} else {
			model.addAttribute("historyBack", true);
		}

		model.addAttribute("alertMsg", msg);

		return "common/redirect";
	}

	@RequestMapping("/article/doAddReply")
	@ResponseBody
	public Map<String, Object> doAddReply(Model model, @RequestParam Map<String, Object> param, HttpSession session,
			HttpServletRequest request) {

		Article article = articleService.getBoardId(param);

		param.put("memberId", session.getAttribute("loginedMemberId"));

		param.put("boardId", article.getBoardId());

		long newId = articleService.addReply(param);

		ArticleReply newReply = articleService.getReply(Maps.of("id", newId));

		Map<String, Object> rs = new HashMap<>();
		rs.put("msg", "댓글이 작성되었습니다.");
		rs.put("resultCode", "S-1");
		rs.put("addedReply", newReply);

		return rs;
	}

	@RequestMapping("/article/doDeleteReply")
	@ResponseBody
	public Map<String, Object> doDeleteReply(Model model, @RequestParam Map<String, Object> param,
			HttpSession session) {

		long loginedId = (long) session.getAttribute("loginedMemberId");
		param.put("loginedMemberId", loginedId);
		Map<String, Object> deleteReplyRs = articleService.deleteReply(param);

		String msg = (String) deleteReplyRs.get("msg");
		String resultCode = (String) deleteReplyRs.get("resultCode");

		Map<String, Object> rs = Maps.of("msg", msg, "resultCode", resultCode);

		return rs;
	}

	@RequestMapping("/article/doModifyReply")
	@ResponseBody
	public Map<String, Object> doModifyReply(Model model, @RequestParam Map<String, Object> param, HttpSession session,
			HttpServletRequest request, long id) {

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		Map<String, Object> rs = new HashMap<>();
		String msg = "";
		String resultCode = "";

		long loginedMemberId = (long) session.getAttribute("loginedMemberId");

		ArticleReply ar = articleService.getReply(param);

		if ( loginedMemberId != ar.getMemberId() ) {
			msg = "댓글을 수정할 권한이 없습니다.";
			resultCode = "F-5";

			rs = Maps.of("msg", msg, "resultCode", resultCode);

			return rs;
		}
		
		param.put("id", id);

		Map<String, Object> updateRs = articleService.updateReply(param);

		msg = (String) updateRs.get("msg");
		resultCode = (String) updateRs.get("resultCode");

		rs = Maps.of("msg", msg, "resultCode", resultCode);

		return rs;
	}
}